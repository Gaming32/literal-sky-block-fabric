package io.github.gaming32.literalskyblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VoidBlockEntity extends SkyBlockEntity {
    public VoidBlockEntity(BlockPos pos, BlockState state) {
        super(LSBBlockEntities.VOID_BLOCK, pos, state);
    }
}
