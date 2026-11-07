package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CriticalHitEvent extends PlayerEvent {
    private final Entity target;
    private final float vanillaDmgMultiplier;
    private final boolean isVanillaCrit;
    private float dmgMultiplier;
    private boolean isCriticalHit;

    public CriticalHitEvent(Player player, Entity target, float dmgMultiplier, boolean isCriticalHit) {
        super(player);
        this.target = target;
        this.dmgMultiplier = this.vanillaDmgMultiplier = dmgMultiplier;
        this.isCriticalHit = this.isVanillaCrit = isCriticalHit;
    }

    public Entity getTarget() {
        return target;
    }

    public float getDamageMultiplier() {
        return dmgMultiplier;
    }

    public void setDamageMultiplier(float dmgMultiplier) {
        this.dmgMultiplier = dmgMultiplier;
    }

    public boolean isCriticalHit() {
        return isCriticalHit;
    }

    public void setCriticalHit(boolean isCriticalHit) {
        this.isCriticalHit = isCriticalHit;
    }

    public float getVanillaMultiplier() {
        return vanillaDmgMultiplier;
    }

    public boolean isVanillaCritical() {
        return isVanillaCrit;
    }
}
