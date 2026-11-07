package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IItemExtension;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements IItemExtension {
}
