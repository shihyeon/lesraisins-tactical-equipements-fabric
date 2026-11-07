package kr.shihyeon.mods.lrtactical.util;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.EffectCure;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EffectCureHelper {
    public static boolean removeEffectsCuredBy(LivingEntity entity, EffectCure cure) {
        if (entity.level().isClientSide) {
            return false;
        }
        List<Holder<MobEffect>> cured = entity.getActiveEffects().stream()
                .filter(instance -> curedBy(instance, cure))
                .map(MobEffectInstance::getEffect)
                .toList();
        boolean removed = false;
        for (Holder<MobEffect> effect : cured) {
            removed |= entity.removeEffect(effect);
        }
        return removed;
    }

    private static boolean curedBy(MobEffectInstance instance, EffectCure cure) {
        Set<EffectCure> cures = new HashSet<>();
        instance.getEffect().value().fillEffectCures(cures, instance);
        return cures.contains(cure);
    }

    private EffectCureHelper() {
    }
}
