package io.github.gaming32.literalskyblock.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import io.github.gaming32.literalskyblock.client.LSBClient;
import io.github.gaming32.literalskyblock.forge.ForgeHooksClient;
import io.github.gaming32.literalskyblock.forge.RegisterShadersEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Shadow @Final private Minecraft minecraft;

    @Unique
    private ResourceManager forge$resourceManager;

    @Inject(method = "reloadShaders", at = @At("HEAD"))
    private void storeResourceManager(ResourceManager manager, CallbackInfo ci) {
        forge$resourceManager = manager;
    }

    @Redirect(
        method = "reloadShaders",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
        )
    )
    private boolean implementShadersEvent(List<Pair<ShaderInstance, Consumer<ShaderInstance>>> instance, Object e) throws IOException {
        @SuppressWarnings("unchecked")
        final var shader = (Pair<ShaderInstance, Consumer<ShaderInstance>>)e;
        instance.add(shader);
        if (shader.getFirst().getName().equals("rendertype_crumbling")) {
            LSBClient.registerShaders(new RegisterShadersEvent(forge$resourceManager, instance));
        }
        return true;
    }

    @Inject(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void implementRenderLast(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo ci, boolean bl, Camera camera, PoseStack poseStack, double d, float f, Matrix4f matrix4f, Matrix3f matrix3f) {
        minecraft.getProfiler().popPush("forge_render_last");
        ForgeHooksClient.dispatchRenderLast(minecraft.levelRenderer, matrix, tickDelta, matrix4f, limitTime);
    }
}
