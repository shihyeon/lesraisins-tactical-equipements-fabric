package kr.shihyeon.mods.lrtactical.neoforged.fml;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ModContainer {
    private final String modId;
    private final Map<Class<?>, Supplier<?>> extensionPoints = new ConcurrentHashMap<>();

    public ModContainer(String modId) {
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    public void registerConfig(ModConfig.Type type, IConfigSpec configSpec) {
        NeoForgeConfigRegistry.INSTANCE.register(modId, type, configSpec);
    }

    public void registerConfig(ModConfig.Type type, IConfigSpec configSpec, String fileName) {
        NeoForgeConfigRegistry.INSTANCE.register(modId, type, configSpec, fileName);
    }

    public <T extends IExtensionPoint> void registerExtensionPoint(Class<? extends T> point, T extension) {
        extensionPoints.put(point, () -> extension);
    }

    public <T extends IExtensionPoint> void registerExtensionPoint(Class<? extends T> point, Supplier<? extends T> extension) {
        extensionPoints.put(point, extension);
    }

    @SuppressWarnings("unchecked")
    public <T extends IExtensionPoint> Optional<T> getCustomExtension(Class<? extends T> point) {
        Supplier<?> supplier = extensionPoints.get(point);
        return supplier == null ? Optional.empty() : Optional.ofNullable((T) supplier.get());
    }
}
