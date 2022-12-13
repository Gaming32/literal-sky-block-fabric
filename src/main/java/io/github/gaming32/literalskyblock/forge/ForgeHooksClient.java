package io.github.gaming32.literalskyblock.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import io.github.gaming32.literalskyblock.client.LSBClient;
import net.minecraft.client.renderer.LevelRenderer;

public class ForgeHooksClient {
    public static void dispatchRenderLast(LevelRenderer context, PoseStack poseStack, float partialTick, Matrix4f projectionMatrix, long finishTimeNano) {
        LSBClient.renderSky(new RenderLevelLastEvent(context, poseStack, partialTick, projectionMatrix, finishTimeNano));
    }
}
