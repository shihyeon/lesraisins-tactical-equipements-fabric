package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/*
 * Injected into ItemStack with Loom interface injection (see fabric.mod.json) and added at runtime by
 * ItemStackHooksMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface IItemStackExtension {
    default int getEnchantmentLevel(Holder<Enchantment> enchantment) {
        return ((ItemStack) this).getEnchantments().getLevel(enchantment);
    }
}
