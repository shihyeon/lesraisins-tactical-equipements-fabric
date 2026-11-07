package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Fabric has no explosion events, so explosions are never cancelled or changed here.
 */
public final class EventHooks {
    public static boolean onExplosionStart(Level level, Explosion explosion) {
        return false;
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> list, double diameter) {
    }

    private EventHooks() {
    }
}
