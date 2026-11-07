package kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.IEventBus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegister<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String namespace;
    private final Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private boolean registered;

    protected DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
        return new DeferredRegister<>(registry.key(), namespace);
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return new DeferredRegister<>(key, namespace);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
        return register(name, id -> supplier.get());
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> factory) {
        if (registered) {
            throw new IllegalStateException("Cannot register new entries to DeferredRegister after it was registered");
        }
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        DeferredHolder<T, I> holder = DeferredHolder.create(ResourceKey.create(registryKey, id));
        entries.put(holder, () -> factory.apply(id));
        return holder;
    }

    public void register(IEventBus bus) {
        bus.addListener(RegisterEvent.class, event -> addEntries());
    }

    @SuppressWarnings("unchecked")
    private void addEntries() {
        if (registered) {
            return;
        }
        registered = true;
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null) {
            throw new IllegalStateException("Unknown registry " + registryKey.location());
        }
        entries.forEach((holder, supplier) -> {
            Registry.register(registry, holder.getKey(), supplier.get());
            holder.bind(false);
        });
    }

    public Collection<DeferredHolder<T, ? extends T>> getEntries() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public String getNamespace() {
        return namespace;
    }
}
