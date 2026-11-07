package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.world.entity.Entity;

public abstract class EntityEvent extends Event {
    private final Entity entity;

    protected EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
