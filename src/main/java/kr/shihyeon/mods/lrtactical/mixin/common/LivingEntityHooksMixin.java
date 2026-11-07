package kr.shihyeon.mods.lrtactical.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.EffectCures;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import kr.shihyeon.mods.lrtactical.util.EffectCureHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntity.class)
public abstract class LivingEntityHooksMixin {
    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDamageSourceBlocked(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    private boolean lrtactical$shieldBlockEvent(LivingEntity self, DamageSource source, Operation<Boolean> original,
                                                @Local(argsOnly = true) float amount,
                                                @Share("shieldEvent") LocalRef<LivingShieldBlockEvent> shieldEvent) {
        boolean blocked = original.call(self, source);
        LivingShieldBlockEvent event = NeoForge.EVENT_BUS.post(new LivingShieldBlockEvent(self, source, amount, blocked));
        shieldEvent.set(event);
        return event.getBlocked();
    }

    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"))
    private void lrtactical$shieldDamage(LivingEntity self, float amount, Operation<Void> original,
                                         @Share("shieldEvent") LocalRef<LivingShieldBlockEvent> shieldEvent) {
        float shieldDamage = shieldEvent.get() != null ? shieldEvent.get().shieldDamage() : amount;
        if (shieldDamage > 0) {
            original.call(self, shieldDamage);
        }
    }

    // Vanilla blocks everything (amount = 0.0F right after the shield call); keep what the event did not block
    @ModifyExpressionValue(
            method = "hurt",
            at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"))
    )
    private float lrtactical$keepUnblockedDamage(float blockedAmount,
                                                 @Share("shieldEvent") LocalRef<LivingShieldBlockEvent> shieldEvent) {
        LivingShieldBlockEvent event = shieldEvent.get();
        return event != null ? event.getOriginalBlockedDamage() - event.getBlockedDamage() : blockedAmount;
    }

    @ModifyReturnValue(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"))
    private float lrtactical$livingDamagePre(float damage, @Local(argsOnly = true) DamageSource source) {
        if (!NeoForge.EVENT_BUS.hasListeners(LivingDamageEvent.Pre.class)) {
            return damage;
        }
        return NeoForge.EVENT_BUS.post(new LivingDamageEvent.Pre((LivingEntity) (Object) this, source, damage)).getNewDamage();
    }

    @WrapOperation(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
    private boolean lrtactical$totemCures(LivingEntity self, Operation<Boolean> original) {
        return EffectCureHelper.removeEffectsCuredBy(self, EffectCures.PROTECTED_BY_TOTEM);
    }

    @WrapMethod(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V")
    private void lrtactical$onEntitySwing(InteractionHand hand, boolean updateSelf, Operation<Void> original) {
        LivingEntity self = (LivingEntity) (Object) this;
        ItemStack stack = self.getItemInHand(hand);
        if (stack.isEmpty() || !stack.getItem().onEntitySwing(stack, self)) {
            original.call(hand, updateSelf);
        }
    }
}
