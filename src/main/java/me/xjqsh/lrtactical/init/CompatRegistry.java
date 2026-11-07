package me.xjqsh.lrtactical.init;

import kr.shihyeon.mods.lrtactical.neoforged.fml.ModList;

public final class CompatRegistry {
    public static final String CLOTH_CONFIG = "cloth_config";
    public static final String PLAYER_ANIMATOR = "playeranimator";

    public static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }

    private CompatRegistry() {
    }
}
