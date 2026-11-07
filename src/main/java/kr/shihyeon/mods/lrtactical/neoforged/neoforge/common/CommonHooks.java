package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class CommonHooks {
    /**
     * Fabric's closest match to NeoForge's AttackEntityEvent. Any result other than PASS stops the attack,
     * which is how protection mods refuse it.
     */
    public static boolean onPlayerAttackTarget(Player player, Entity target) {
        if (player.isSpectator()) {
            return false;
        }
        InteractionResult result = AttackEntityCallback.EVENT.invoker()
                .interact(player, player.level(), InteractionHand.MAIN_HAND, target, null);
        return result == InteractionResult.PASS;
    }

    public static CriticalHitEvent fireCriticalHit(Player player, Entity target, boolean vanillaCritical, float damageModifier) {
        return NeoForge.EVENT_BUS.post(new CriticalHitEvent(player, target, damageModifier, vanillaCritical));
    }

    private CommonHooks() {
    }
}
