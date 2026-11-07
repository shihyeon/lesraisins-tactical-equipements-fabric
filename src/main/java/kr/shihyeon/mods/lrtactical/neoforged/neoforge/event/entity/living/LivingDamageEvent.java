package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public abstract class LivingDamageEvent extends LivingEvent {
    protected LivingDamageEvent(LivingEntity entity) {
        super(entity);
    }

    /**
     * Posted from LivingEntityHooksMixin with the damage left after armor and enchantment reduction,
     * for mobs and players alike.
     */
    public static class Pre extends LivingDamageEvent {
        private final DamageSource source;
        private final float originalDamage;
        private float newDamage;

        public Pre(LivingEntity entity, DamageSource source, float damage) {
            super(entity);
            this.source = source;
            this.originalDamage = damage;
            this.newDamage = damage;
        }

        public DamageSource getSource() {
            return source;
        }

        public float getOriginalDamage() {
            return originalDamage;
        }

        public float getNewDamage() {
            return newDamage;
        }

        public void setNewDamage(float newDamage) {
            this.newDamage = newDamage;
        }
    }
}
