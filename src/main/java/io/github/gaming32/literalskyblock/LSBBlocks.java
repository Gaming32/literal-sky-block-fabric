package io.github.gaming32.literalskyblock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LSBBlocks {
    public static final Block SKY_BLOCK = Registry.register(
        Registry.BLOCK,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "sky_block"),
        new SkyBlock(
            BlockBehaviour.Properties.copy(Blocks.STONE)
                .noOcclusion()
                .isValidSpawn((blockState, blockGetter, blockPos, object) -> false)
                .isRedstoneConductor((blockState, blockGetter, blockPos) -> false)
                .isSuffocating((blockState, blockGetter, blockPos) -> false)
                .isViewBlocking((blockState, blockGetter, blockPos) -> false)
                .lightLevel(state -> 15)
        )
    );
    public static final Block VOID_BLOCK = Registry.register(
        Registry.BLOCK,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "void_block"),
        new VoidBlock(
            BlockBehaviour.Properties.copy(Blocks.STONE)
        )
    );

    static void register() {
    }
}
