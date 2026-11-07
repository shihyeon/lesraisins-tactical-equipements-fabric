package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IFoodPropertiesBuilderExtension;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FoodProperties.Builder.class)
public abstract class FoodPropertiesBuilderMixin implements IFoodPropertiesBuilderExtension {
}
