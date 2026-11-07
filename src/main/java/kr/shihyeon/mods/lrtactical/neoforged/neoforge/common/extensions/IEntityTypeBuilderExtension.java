package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/*
 * Injected into EntityType.Builder with Loom interface injection (see fabric.mod.json) and added at runtime by
 * EntityTypeBuilderMixin, so upstream code that calls these NeoForge methods compiles unchanged.
 */
public interface IEntityTypeBuilderExtension<T extends Entity> {
    @SuppressWarnings("unchecked")
    private EntityType.Builder<T> self() {
        return (EntityType.Builder<T>) (Object) this;
    }

    default EntityType.Builder<T> setShouldReceiveVelocityUpdates(boolean value) {
        return self().alwaysUpdateVelocity(value);
    }

    default EntityType.Builder<T> setTrackingRange(int range) {
        return self().clientTrackingRange(range);
    }

    default EntityType.Builder<T> setUpdateInterval(int interval) {
        return self().updateInterval(interval);
    }
}
