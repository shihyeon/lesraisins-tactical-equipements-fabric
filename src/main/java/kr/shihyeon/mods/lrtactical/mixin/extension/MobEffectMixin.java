package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IMobEffectExtension;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements IMobEffectExtension {
}
