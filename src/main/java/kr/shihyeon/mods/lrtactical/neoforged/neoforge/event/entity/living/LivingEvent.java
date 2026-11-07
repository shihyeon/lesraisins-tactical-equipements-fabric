package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.living;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;

public abstract class LivingEvent extends EntityEvent {
    private final LivingEntity livingEntity;

    protected LivingEvent(LivingEntity entity) {
        super(entity);
        this.livingEntity = entity;
    }

    @Override
    public LivingEntity getEntity() {
        return livingEntity;
    }
}
