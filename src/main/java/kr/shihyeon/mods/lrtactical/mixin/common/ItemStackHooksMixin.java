package kr.shihyeon.mods.lrtactical.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IItemStackExtension;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackHooksMixin implements IItemStackExtension {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isEmpty();

    @ModifyReturnValue(method = "getMaxStackSize", at = @At("RETURN"))
    private int lrtactical$itemMaxStackSize(int original) {
        return this.getItem().getMaxStackSize((ItemStack) (Object) this);
    }

    @ModifyReturnValue(method = "getMaxDamage", at = @At("RETURN"))
    private int lrtactical$itemMaxDamage(int original) {
        return this.getItem().getMaxDamage((ItemStack) (Object) this);
    }

    @ModifyExpressionValue(
            method = {
                    "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
                    "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
                    "addAttributeTooltips"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;")
    )
    private Object lrtactical$itemAttributeModifierEvent(Object original) {
        if (!(original instanceof ItemAttributeModifiers modifiers) || this.isEmpty()
                || !NeoForge.EVENT_BUS.hasListeners(ItemAttributeModifierEvent.class)) {
            return original;
        }
        if (modifiers.modifiers().isEmpty()) {
            modifiers = this.getItem().getDefaultAttributeModifiers();
        }
        return NeoForge.EVENT_BUS.post(new ItemAttributeModifierEvent((ItemStack) (Object) this, modifiers)).build();
    }
}
