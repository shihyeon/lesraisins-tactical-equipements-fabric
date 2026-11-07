package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/*
 * Injected into Block with Loom interface injection (see fabric.mod.json) and added at runtime by
 * BlockMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface IBlockExtension {
    default SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return state.getSoundType();
    }
}
