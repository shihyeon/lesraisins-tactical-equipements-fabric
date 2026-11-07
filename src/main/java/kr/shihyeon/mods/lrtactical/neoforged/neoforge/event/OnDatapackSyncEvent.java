package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Fabric syncs each player on its own, so this is posted once per player (on join and after /reload).
 */
public class OnDatapackSyncEvent extends Event {
    private final PlayerList playerList;
    @Nullable
    private final ServerPlayer player;

    public OnDatapackSyncEvent(PlayerList playerList, @Nullable ServerPlayer player) {
        this.playerList = playerList;
        this.player = player;
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return player;
    }

    public Stream<ServerPlayer> getRelevantPlayers() {
        return player == null ? playerList.getPlayers().stream() : Stream.of(player);
    }
}
