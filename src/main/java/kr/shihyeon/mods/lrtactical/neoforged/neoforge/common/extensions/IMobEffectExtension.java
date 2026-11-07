package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.EffectCure;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.EffectCures;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Set;

/*
 * Injected into MobEffect with Loom interface injection (see fabric.mod.json) and added at runtime by
 * MobEffectMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface IMobEffectExtension {
    /**
     * Milk and totems only remove effects that list them here (see EffectCureHelper).
     */
    default void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        cures.addAll(EffectCures.DEFAULT_CURES);
    }
}
