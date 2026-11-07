package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IBlockExtension;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public abstract class BlockMixin implements IBlockExtension {
}
