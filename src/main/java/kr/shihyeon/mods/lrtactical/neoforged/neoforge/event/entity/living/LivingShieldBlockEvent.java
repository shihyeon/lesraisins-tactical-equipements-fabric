package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.living;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.ICancellableEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

/**
 * Posted from LivingEntityHooksMixin whenever an entity takes positive damage, blocked or not, like on NeoForge.
 */
public class LivingShieldBlockEvent extends LivingEvent implements ICancellableEvent {
    private final DamageSource source;
    private final float originalDamage;
    private final boolean originalBlocked;
    private float dmgBlocked;
    private float shieldDamage;
    private boolean newBlocked;

    public LivingShieldBlockEvent(LivingEntity blocker, DamageSource source, float damage, boolean originalBlockedState) {
        super(blocker);
        this.source = source;
        this.originalDamage = damage;
        this.dmgBlocked = damage;
        this.shieldDamage = damage;
        this.originalBlocked = originalBlockedState;
        this.newBlocked = originalBlockedState;
    }

    public DamageSource getDamageSource() {
        return source;
    }

    public float getOriginalBlockedDamage() {
        return originalDamage;
    }

    public float getBlockedDamage() {
        return Math.min(dmgBlocked, originalDamage);
    }

    public float shieldDamage() {
        if (newBlocked) {
            return shieldDamage >= 0 ? shieldDamage : getBlockedDamage();
        }
        return 0;
    }

    public void setBlockedDamage(float blocked) {
        this.dmgBlocked = Mth.clamp(blocked, 0, originalDamage);
    }

    public void setShieldDamage(float damage) {
        this.shieldDamage = damage;
    }

    public boolean getOriginalBlock() {
        return originalBlocked;
    }

    public boolean getBlocked() {
        return newBlocked && !isCanceled();
    }

    public void setBlocked(boolean isBlocked) {
        this.newBlocked = isBlocked;
    }
}
