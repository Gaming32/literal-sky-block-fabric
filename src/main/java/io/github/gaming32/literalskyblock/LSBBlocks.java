package io.github.gaming32.literalskyblock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class LSBBlocks {
    public static final Block SKY_BLOCK = Registry.register(
        Registry.BLOCK,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "sky_block"),
        new SkyBlock()
    );
    public static final Block VOID_BLOCK = Registry.register(
        Registry.BLOCK,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "void_block"),
        new VoidBlock()
    );

    static void register() {
    }
}
