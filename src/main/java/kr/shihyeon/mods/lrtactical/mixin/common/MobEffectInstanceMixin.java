package kr.shihyeon.mods.lrtactical.mixin.common;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores the registry's own holder instead of a {@link DeferredHolder}, so the active effect map of an
 * entity is always keyed by the same object as effects read from saves and packets (NeoForge: getDelegate()).
 */
@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {
    @Shadow
    @Final
    @Mutable
    private Holder<MobEffect> effect;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void lrtactical$unwrapDeferredHolder(CallbackInfo ci) {
        if (this.effect instanceof DeferredHolder<MobEffect, ?> deferred) {
            this.effect = deferred.getDelegate();
        }
    }
}
