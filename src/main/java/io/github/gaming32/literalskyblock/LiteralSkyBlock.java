package io.github.gaming32.literalskyblock;

import net.fabricmc.api.ModInitializer;

public class LiteralSkyBlock implements ModInitializer {
    public static final String MOD_ID = "literalskyblock";

    @Override
    public void onInitialize() {
        LSBBlocks.register();
        LSBItems.register();
        LSBBlockEntities.register();
    }
}
