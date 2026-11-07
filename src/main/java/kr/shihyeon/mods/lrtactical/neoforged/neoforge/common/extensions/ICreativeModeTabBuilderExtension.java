package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

/*
 * Injected into CreativeModeTab.Builder with Loom interface injection (see fabric.mod.json) and added at runtime by
 * CreativeModeTabBuilderMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface ICreativeModeTabBuilderExtension {
    /**
     * Fabric orders mod tabs by registration, which already follows the DeferredRegister order.
     */
    default CreativeModeTab.Builder withTabsBefore(ResourceLocation... tabs) {
        return (CreativeModeTab.Builder) this;
    }

    @SuppressWarnings("unchecked")
    default CreativeModeTab.Builder withTabsBefore(ResourceKey<CreativeModeTab>... tabs) {
        return (CreativeModeTab.Builder) this;
    }

    default CreativeModeTab.Builder withTabsAfter(ResourceLocation... tabs) {
        return (CreativeModeTab.Builder) this;
    }
}
