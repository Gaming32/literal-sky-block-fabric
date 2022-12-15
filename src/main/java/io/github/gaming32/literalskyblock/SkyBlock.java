package io.github.gaming32.literalskyblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkyBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public SkyBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, true).setValue(POWERED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final boolean powered = ctx.getLevel().hasNeighborSignal(ctx.getClickedPos());
        return this.defaultBlockState().setValue(ACTIVE, !powered).setValue(POWERED, powered);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SkyBlockEntity(pos, state);
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof SkyBlockEntity skyBlockEntity) {
            skyBlockEntity.neighborChanged();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborChanged(state, level, pos, block, fromPos, notify);

        final BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof SkyBlockEntity skyBlockEntity) {
            skyBlockEntity.neighborChanged();
        }

        if (!level.isClientSide) {
            boolean wasPowered = state.getValue(POWERED);
            if (wasPowered != level.hasNeighborSignal(pos)) {
                BlockState newState = state.cycle(POWERED);
                if (!wasPowered) {
                    newState = newState.cycle(ACTIVE);
                }
                level.setBlock(pos, newState, 2);
            }
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return !(this instanceof VoidBlock) || super.propagatesSkylightDown(state, world, pos);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(ACTIVE) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, POWERED);
    }
}
