package kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryBuilder<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private boolean sync;

    public RegistryBuilder(ResourceKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    public RegistryBuilder<T> sync(boolean sync) {
        this.sync = sync;
        return this;
    }

    @SuppressWarnings("unchecked")
    public Registry<T> create() {
        var builder = FabricRegistryBuilder.createSimple((ResourceKey<Registry<T>>) registryKey);
        if (sync) {
            builder.attribute(RegistryAttribute.SYNCED);
        }
        return builder.buildAndRegister();
    }
}
