package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.client.DeltaTracker;

public abstract class RenderFrameEvent extends Event {
    private final DeltaTracker partialTick;

    protected RenderFrameEvent(DeltaTracker partialTick) {
        this.partialTick = partialTick;
    }

    public DeltaTracker getPartialTick() {
        return partialTick;
    }

    public static class Pre extends RenderFrameEvent {
        public Pre(DeltaTracker partialTick) {
            super(partialTick);
        }
    }

    public static class Post extends RenderFrameEvent {
        public Post(DeltaTracker partialTick) {
            super(partialTick);
        }
    }
}
