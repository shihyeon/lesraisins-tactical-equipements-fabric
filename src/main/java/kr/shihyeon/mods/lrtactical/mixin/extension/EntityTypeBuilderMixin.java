package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IEntityTypeBuilderExtension;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityType.Builder.class)
public abstract class EntityTypeBuilderMixin<T extends net.minecraft.world.entity.Entity> implements IEntityTypeBuilderExtension<T> {
}
