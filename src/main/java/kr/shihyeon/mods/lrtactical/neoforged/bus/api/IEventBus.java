package kr.shihyeon.mods.lrtactical.neoforged.bus.api;

import java.util.function.Consumer;

public interface IEventBus {
    void register(Object target);

    <T extends Event> void addListener(Class<T> eventType, Consumer<T> listener);

    <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> listener);

    <T extends Event> T post(T event);

    boolean hasListeners(Class<? extends Event> eventType);
}
