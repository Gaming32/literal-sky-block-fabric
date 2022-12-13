package io.github.gaming32.literalskyblock.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import io.github.gaming32.literalskyblock.SkyBlockEntity;
import io.github.gaming32.literalskyblock.VoidBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class SkyBlockEntityRenderer implements BlockEntityRenderer<SkyBlockEntity> {
    public SkyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SkyBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource source, int light, int block) {
        final Matrix4f matrix4f = poseStack.last().pose();
        final boolean solid = LSBClient.needIrisCompat && IrisCompat.shadersEnabled();
        renderCube(entity, matrix4f, source.getBuffer(
            solid
                ? RenderType.solid()
                : entity instanceof VoidBlockEntity
                    ? RenderType.endGateway()
                    : LSBClient.SKY_RENDER_TYPE
        ), solid);
        LSBClient.updateSky = true;
    }

    private void renderCube(SkyBlockEntity entity, Matrix4f matrix, VertexConsumer buffer, boolean block) {
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, Direction.SOUTH, block);
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Direction.NORTH, block);
        renderFace(entity, matrix, buffer, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.EAST, block);
        renderFace(entity, matrix, buffer, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.WEST, block);
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, Direction.DOWN, block);
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, Direction.UP, block);
    }

    private void renderFace(SkyBlockEntity entity, Matrix4f matrix, VertexConsumer buffer, float f, float g, float h, float i, float j, float k, float l, float m, Direction direction, boolean block) {
        if (entity.shouldRenderFace(direction)) {
            block(entity, buffer.vertex(matrix, f, h, j), direction, block).endVertex();
            block(entity, buffer.vertex(matrix, g, h, k), direction, block).endVertex();
            block(entity, buffer.vertex(matrix, g, i, l), direction, block).endVertex();
            block(entity, buffer.vertex(matrix, f, i, m), direction, block).endVertex();
        }
    }

    private VertexConsumer block(SkyBlockEntity entity, VertexConsumer consumer, Direction direction, boolean block) {
        if (block) {
            if (entity instanceof VoidBlockEntity) {
                consumer.color(0.1f, 0.1f, 0.2f, 1f);
            } else {
                consumer.color(0.47058824f, 0.654902f, 1f, 1f);
            }
            return consumer.uv(0f, 0f)
                .uv2(32767, 32767)
                .normal(-direction.getStepX(), -direction.getStepY(), -direction.getStepZ());
        }
        return consumer;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
