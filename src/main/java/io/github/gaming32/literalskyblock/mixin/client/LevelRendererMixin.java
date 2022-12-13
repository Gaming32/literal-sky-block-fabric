package io.github.gaming32.literalskyblock.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import io.github.gaming32.literalskyblock.client.LSBClient;
import io.github.gaming32.literalskyblock.client.LevelRendererLSB;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements LevelRendererLSB {
    @Shadow @Final private RenderBuffers renderBuffers;

    @Shadow protected abstract void renderSnowAndRain(LightTexture manager, float f, double d, double e, double g);

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch(Lnet/minecraft/client/renderer/RenderType;)V", ordinal = 6, shift = At.Shift.AFTER))
    private void renderLevelLSB(PoseStack poseStack, float delta, long time, boolean blockOutlines, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix, CallbackInfo ci) {
        renderBuffers.bufferSource().endBatch(LSBClient.SKY_RENDER_TYPE);
    }

    @Override
    public void renderSnowAndRainLSB(LightTexture arg, float g, double d, double e, double h) {
        renderSnowAndRain(arg, g, d, e, h);
    }
}
