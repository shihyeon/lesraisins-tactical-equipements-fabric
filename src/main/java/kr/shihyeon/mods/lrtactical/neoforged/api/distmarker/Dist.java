package kr.shihyeon.mods.lrtactical.neoforged.api.distmarker;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public enum Dist {
    CLIENT,
    DEDICATED_SERVER;

    public boolean isClient() {
        return this == CLIENT;
    }

    public boolean isDedicatedServer() {
        return this == DEDICATED_SERVER;
    }

    public static Dist current() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? CLIENT : DEDICATED_SERVER;
    }
}
