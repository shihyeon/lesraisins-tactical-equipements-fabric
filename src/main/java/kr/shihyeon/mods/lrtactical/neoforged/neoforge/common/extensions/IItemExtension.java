package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.ItemAbility;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/*
 * Injected into Item with Loom interface injection (see fabric.mod.json) and added at runtime by
 * ItemMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface IItemExtension {
    private Item self() {
        return (Item) this;
    }

    default void initializeClient(Consumer<IClientItemExtensions> consumer) {
    }

    default int getMaxStackSize(ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
    }

    default int getMaxDamage(ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
    }

    default boolean isDamageable(ItemStack stack) {
        return stack.has(DataComponents.MAX_DAMAGE);
    }

    default int getEnchantmentValue(ItemStack stack) {
        return self().getEnchantmentValue();
    }

    default boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    /**
     * Only {@code ItemAbilities.SWORD_SWEEP} is consulted on Fabric (PlayerHooksMixin).
     */
    default boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return false;
    }

    @Nullable
    default FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return stack.get(DataComponents.FOOD);
    }

    default void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
    }
}
