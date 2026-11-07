package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.player;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.entity.living.LivingEvent;
import net.minecraft.world.entity.player.Player;

public abstract class PlayerEvent extends LivingEvent {
    private final Player player;

    protected PlayerEvent(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public Player getEntity() {
        return player;
    }
}
