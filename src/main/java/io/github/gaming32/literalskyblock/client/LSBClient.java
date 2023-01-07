package io.github.gaming32.literalskyblock.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import io.github.gaming32.literalskyblock.LSBBlockEntities;
import io.github.gaming32.literalskyblock.LiteralSkyBlock;
import io.github.gaming32.literalskyblock.forge.HackShaderInstanceResourceLocation;
import io.github.gaming32.literalskyblock.forge.RegisterShadersEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

@Environment(EnvType.CLIENT)
public class LSBClient implements ClientModInitializer {
    private static ShaderInstance skyShader;
    private static int skyWidth = -1;
    private static int skyHeight = -1;
    private static TextureTarget skyRenderTarget;
    public static boolean updateSky = false;
    private static boolean isRenderingSky = false;
    static boolean needIrisCompat;

    public static ShaderInstance getSkyShader() {
        return skyShader;
    }

    public static void setSkyShader(ShaderInstance shader) {
        skyShader = shader;
    }

    public static final RenderType SKY_RENDER_TYPE = RenderType.create(LiteralSkyBlock.MOD_ID + "_sky", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
        .setShaderState(new RenderStateShard.ShaderStateShard(LSBClient::getSkyShader))
        .setTextureState(new RenderStateShard.EmptyTextureStateShard(
            () -> {
                if (skyRenderTarget != null) {
                    RenderSystem.setShaderTexture(0, skyRenderTarget.getColorTextureId());
                } else {
                    RenderSystem.setShaderTexture(0, 0);
                }
            },
            () -> {}
        ))
        .createCompositeState(false)
    );

    public static void renderSky(WorldRenderContext event) {
        if (isRenderingSky) return;

        final Minecraft mc = Minecraft.getInstance();
        final Window window = mc.getWindow();
        final int ww = window.getWidth();
        final int wh = window.getHeight();

        if (ww <= 0 || wh <= 0) return;

        boolean update = false;

        if (skyRenderTarget == null || skyWidth != ww || skyHeight != wh) {
            update = true;
            skyWidth = ww;
            skyHeight = wh;
        }

        if (update) {
            if (skyRenderTarget != null) {
                skyRenderTarget.destroyBuffers();
            }

            skyRenderTarget = new TextureTarget(skyWidth, skyHeight, true, Minecraft.ON_OSX);
        }

        if (needIrisCompat && IrisCompat.shadersEnabled()) {
            return;
        }
        if (needIrisCompat) IrisCompat.preRender(mc.levelRenderer);
        mc.gameRenderer.setRenderBlockOutline(false);
        mc.levelRenderer.graphicsChanged();
        skyRenderTarget.bindWrite(true);

        isRenderingSky = true;
        final RenderTarget mainRenderTarget = mc.getMainRenderTarget();
        renderActualSky(mc, event);
        isRenderingSky = false;

        mc.gameRenderer.setRenderBlockOutline(true);
        skyRenderTarget.unbindRead();
        skyRenderTarget.unbindWrite();
        mc.levelRenderer.graphicsChanged();
        mainRenderTarget.bindWrite(true);
        if (needIrisCompat) IrisCompat.postRender(mc.levelRenderer);
    }

    public static void renderActualSky(Minecraft mc, WorldRenderContext event) {
        final PoseStack poseStack = event.matrixStack();
        final float delta = event.tickDelta();
        final Matrix4f projectionMatrix = event.projectionMatrix();
        final LevelRenderer levelRenderer = mc.levelRenderer;
        final LevelRendererLSB levelRendererLSB = (LevelRendererLSB)levelRenderer;
        final GameRenderer gameRenderer = mc.gameRenderer;
        final Camera camera = gameRenderer.getMainCamera();
        final Vec3 cameraPos = camera.getPosition();
        final LightTexture lightTexture = gameRenderer.lightTexture();

        //noinspection DataFlowIssue
        FogRenderer.setupColor(camera, delta, mc.level, mc.options.getEffectiveRenderDistance(), gameRenderer.getDarkenWorldAmount(delta));
        FogRenderer.levelFogColor();
        RenderSystem.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
        final float renderDistance = gameRenderer.getRenderDistance();
        final boolean hasSpecialFog = mc.level.effects().isFoggyAt(Mth.floor(cameraPos.x), Mth.floor(cameraPos.y)) || mc.gui.getBossOverlay().shouldCreateWorldFog();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        levelRenderer.renderSky(poseStack, projectionMatrix, delta, camera, false, () -> FogRenderer.setupFog(camera, FogRenderer.FogMode.FOG_SKY, renderDistance, hasSpecialFog, delta));

        final PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();

        if (mc.options.getCloudsType() != CloudStatus.OFF) {
            RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            levelRenderer.renderClouds(poseStack, projectionMatrix, delta, cameraPos.x, cameraPos.y, cameraPos.z);
        }

        RenderSystem.depthMask(false);
        levelRendererLSB.renderSnowAndRainLSB(lightTexture, delta, cameraPos.x, cameraPos.y, cameraPos.z);
        RenderSystem.depthMask(true);

        RenderSystem.disableBlend();
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        FogRenderer.setupNoFog();
    }

    @Override
    public void onInitializeClient() {
        needIrisCompat = FabricLoader.getInstance().isModLoaded("iris");
        WorldRenderEvents.END.register(LSBClient::renderSky);
        BlockEntityRenderers.register(LSBBlockEntities.SKY_BLOCK, SkyBlockEntityRenderer::new);
        BlockEntityRenderers.register(LSBBlockEntities.VOID_BLOCK, SkyBlockEntityRenderer::new);
    }

    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(HackShaderInstanceResourceLocation.create(event.getResourceManager(), new ResourceLocation(LiteralSkyBlock.MOD_ID, "sky"), DefaultVertexFormat.POSITION), LSBClient::setSkyShader);
    }
}
