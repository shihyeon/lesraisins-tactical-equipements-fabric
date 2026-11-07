package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EffectCure {
    private static final Map<String, EffectCure> CURES = new ConcurrentHashMap<>();

    private final String name;

    private EffectCure(String name) {
        this.name = name;
    }

    public static EffectCure get(String name) {
        return CURES.computeIfAbsent(name, EffectCure::new);
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "EffectCure[" + name + "]";
    }
}
