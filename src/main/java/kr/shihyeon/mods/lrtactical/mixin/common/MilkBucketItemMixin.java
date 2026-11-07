package kr.shihyeon.mods.lrtactical.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import kr.shihyeon.mods.lrtactical.util.EffectCureHelper;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.EffectCures;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.MilkBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin {
    @WrapOperation(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
    private boolean lrtactical$milkCures(LivingEntity entity, Operation<Boolean> original) {
        return EffectCureHelper.removeEffectsCuredBy(entity, EffectCures.MILK);
    }
}
