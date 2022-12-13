package io.github.gaming32.literalskyblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class SkyBlockEntity extends BlockEntity {
    private final Boolean[] shouldRender = new Boolean[6];

    public SkyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public SkyBlockEntity(BlockPos pos, BlockState state) {
        this(LSBBlockEntities.SKY_BLOCK, pos, state);
    }

    public boolean shouldRenderFace(Direction direction) {
        final int index = direction.ordinal();

        if (shouldRender[index] == null) {
            shouldRender[index] = level == null || Block.shouldRenderFace(getBlockState(), level, getBlockPos(), direction, getBlockPos().relative(direction));
        }

        return shouldRender[index];
    }

    public void neighborChanged() {
        Arrays.fill(shouldRender, null);
    }
}
