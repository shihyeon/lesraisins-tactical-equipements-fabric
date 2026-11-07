package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import net.minecraft.world.item.Item;

/*
 * Injected into Item.Properties with Loom interface injection (see fabric.mod.json) and added at runtime by
 * ItemPropertiesMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface IItemPropertiesExtension {
    /**
     * Kept for source compatibility. Fabric has no repair hook, so this changes nothing.
     */
    default Item.Properties setNoRepair() {
        return (Item.Properties) this;
    }
}
