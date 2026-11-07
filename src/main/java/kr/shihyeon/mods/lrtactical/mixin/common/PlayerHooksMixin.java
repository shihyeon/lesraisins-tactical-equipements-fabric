package kr.shihyeon.mods.lrtactical.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.CommonHooks;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.ItemAbilities;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerHooksMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void lrtactical$playerTickPre(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new PlayerTickEvent.Pre((Player) (Object) this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void lrtactical$playerTickPost(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new PlayerTickEvent.Post((Player) (Object) this));
    }

    // The vanilla jump-crit flag, stored right after Item#getAttackDamageBonus
    @ModifyVariable(
            method = "attack",
            at = @At(value = "STORE", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getAttackDamageBonus(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;)F")),
            ordinal = 2
    )
    private boolean lrtactical$criticalHitEvent(boolean vanillaCritical, @Local(argsOnly = true) Entity target,
                                                @Share("critEvent") LocalRef<CriticalHitEvent> critEvent) {
        CriticalHitEvent event = CommonHooks.fireCriticalHit((Player) (Object) this, target, vanillaCritical, vanillaCritical ? 1.5F : 1.0F);
        critEvent.set(event);
        return event.isCriticalHit();
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5"))
    private float lrtactical$criticalHitMultiplier(float vanilla, @Share("critEvent") LocalRef<CriticalHitEvent> critEvent) {
        return critEvent.get() != null ? critEvent.get().getDamageMultiplier() : vanilla;
    }

    // A class literal (not an @At args string), so the target is remapped in production
    @WrapOperation(method = "attack", constant = @Constant(classValue = SwordItem.class))
    private boolean lrtactical$canSweep(Object item, Operation<Boolean> original) {
        if (original.call(item)) {
            return true;
        }
        ItemStack stack = ((Player) (Object) this).getItemInHand(InteractionHand.MAIN_HAND);
        return item instanceof Item held && held.canPerformAction(stack, ItemAbilities.SWORD_SWEEP);
    }
}
