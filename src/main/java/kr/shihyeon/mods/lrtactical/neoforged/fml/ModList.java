package kr.shihyeon.mods.lrtactical.neoforged.fml;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ModList {
    private static final ModList INSTANCE = new ModList();

    // NeoForge mod ids that differ on Fabric
    private static final Map<String, String> FABRIC_IDS = Map.of(
            "cloth_config", "cloth-config"
    );

    private final Map<String, ModContainer> containers = new ConcurrentHashMap<>();

    public static ModList get() {
        return INSTANCE;
    }

    public static String toFabricId(String modId) {
        return FABRIC_IDS.getOrDefault(modId, modId);
    }

    public boolean isLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(toFabricId(modId));
    }

    public Optional<? extends ModContainer> getModContainerById(String modId) {
        if (!isLoaded(modId)) {
            return Optional.empty();
        }
        return Optional.of(containers.computeIfAbsent(toFabricId(modId), ModContainer::new));
    }
}
