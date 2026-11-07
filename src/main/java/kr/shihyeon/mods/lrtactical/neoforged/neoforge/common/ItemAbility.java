package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemAbility {
    private static final Map<String, ItemAbility> ABILITIES = new ConcurrentHashMap<>();

    private final String name;

    private ItemAbility(String name) {
        this.name = name;
    }

    public static ItemAbility get(String name) {
        return ABILITIES.computeIfAbsent(name, ItemAbility::new);
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "ItemAbility[" + name + "]";
    }
}
