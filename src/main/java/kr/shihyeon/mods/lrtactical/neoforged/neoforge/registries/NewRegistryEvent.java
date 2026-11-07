package kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.core.Registry;

/**
 * Registries made by {@link RegistryBuilder#create()} are already added to the root registry,
 * so registering them here only keeps the NeoForge call shape.
 */
public class NewRegistryEvent extends Event {
    public <T> void register(Registry<T> registry) {
    }
}
