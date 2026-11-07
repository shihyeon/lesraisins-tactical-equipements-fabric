package kr.shihyeon.mods.lrtactical.neoforged.fml.event.lifecycle;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Fabric initializes mods on the main thread, so queued work runs right away.
 */
public abstract class ParallelDispatchEvent extends Event {
    public CompletableFuture<Void> enqueueWork(Runnable work) {
        work.run();
        return CompletableFuture.completedFuture(null);
    }

    public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
        return CompletableFuture.completedFuture(work.get());
    }
}
