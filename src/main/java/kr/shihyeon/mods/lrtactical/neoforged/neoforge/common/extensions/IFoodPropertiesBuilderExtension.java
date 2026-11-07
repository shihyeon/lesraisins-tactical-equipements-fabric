package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

/*
 * Injected into FoodProperties.Builder with Loom interface injection (see fabric.mod.json) and added at runtime by
 * FoodPropertiesBuilderMixin, so upstream code that calls these NeoForge methods compiles unchanged.
 */
public interface IFoodPropertiesBuilderExtension {
    /**
     * Vanilla stores the effect itself, so the supplier is read once here. Null effects are skipped.
     */
    default FoodProperties.Builder effect(Supplier<MobEffectInstance> effect, float probability) {
        FoodProperties.Builder builder = (FoodProperties.Builder) this;
        MobEffectInstance instance = effect.get();
        return instance == null ? builder : builder.effect(instance, probability);
    }
}
