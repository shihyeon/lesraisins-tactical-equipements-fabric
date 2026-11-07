package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;

public abstract class ClientTickEvent extends Event {
    public static class Pre extends ClientTickEvent {
    }

    public static class Post extends ClientTickEvent {
    }
}
