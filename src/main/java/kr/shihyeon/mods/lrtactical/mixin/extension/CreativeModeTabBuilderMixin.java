package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.ICreativeModeTabBuilderExtension;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreativeModeTab.Builder.class)
public abstract class CreativeModeTabBuilderMixin implements ICreativeModeTabBuilderExtension {
}
