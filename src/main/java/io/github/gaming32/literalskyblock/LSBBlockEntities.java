package io.github.gaming32.literalskyblock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LSBBlockEntities {
    public static final BlockEntityType<SkyBlockEntity> SKY_BLOCK = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "sky_block"),
        BlockEntityType.Builder.of(SkyBlockEntity::new, LSBBlocks.SKY_BLOCK).build(null)
    );
    public static final BlockEntityType<VoidBlockEntity> VOID_BLOCK = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new ResourceLocation(LiteralSkyBlock.MOD_ID, "void_block"),
        BlockEntityType.Builder.of(VoidBlockEntity::new, LSBBlocks.VOID_BLOCK).build(null)
    );

    static void register() {
    }
}
