package kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.handling;

import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;

public interface IPayloadContext {
    Player player();

    /**
     * Fabric already runs play payload handlers on the main thread, so the work runs right away.
     */
    default CompletableFuture<Void> enqueueWork(Runnable task) {
        task.run();
        return CompletableFuture.completedFuture(null);
    }
}
