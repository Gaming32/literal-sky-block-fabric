package io.github.gaming32.literalskyblock.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.LevelRenderer;

public record RenderLevelLastEvent(
    LevelRenderer levelRenderer,
    PoseStack poseStack,
    float partialTick,
    Matrix4f projectionMatrix,
    long startNanos
) {
}
