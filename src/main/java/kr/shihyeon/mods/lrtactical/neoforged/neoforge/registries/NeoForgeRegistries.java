package kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.attachment.AttachmentType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class NeoForgeRegistries {
    public static final Registry<AttachmentType<?>> ATTACHMENT_TYPES =
            FabricRegistryBuilder.createSimple(Keys.ATTACHMENT_TYPES).buildAndRegister();

    public static final class Keys {
        // Namespaced to this mod so it never clashes with another NeoForge compatibility layer
        public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES =
                ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("lrtactical", "neoforge_attachment_types"));

        private Keys() {
        }
    }

    private NeoForgeRegistries() {
    }
}
