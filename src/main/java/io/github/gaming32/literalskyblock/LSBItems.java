package io.github.gaming32.literalskyblock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class LSBItems {
    public static final Item SKY_BLOCK = Registry.register(
        Registry.ITEM,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "sky_block"),
        new BlockItem(LSBBlocks.SKY_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
    );
    public static final Item VOID_BLOCK = Registry.register(
        Registry.ITEM,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "void_block"),
        new BlockItem(LSBBlocks.VOID_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
    );

    static void register() {
    }
}
