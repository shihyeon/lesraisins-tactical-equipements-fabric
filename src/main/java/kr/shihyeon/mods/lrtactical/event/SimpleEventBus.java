package kr.shihyeon.mods.lrtactical.event;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.EventPriority;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.ICancellableEvent;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.IEventBus;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.SubscribeEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public final class SimpleEventBus implements IEventBus {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Listener[] NO_LISTENERS = new Listener[0];

    private final String name;
    private final List<Listener> listeners = new CopyOnWriteArrayList<>();
    private final Map<Class<?>, Listener[]> byEventType = new ConcurrentHashMap<>();
    private final AtomicLong order = new AtomicLong();

    public SimpleEventBus(String name) {
        this.name = name;
    }

    @Override
    public void register(Object target) {
        boolean statics = target instanceof Class<?>;
        Class<?> type = statics ? (Class<?>) target : target.getClass();
        int found = 0;
        for (Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
                if (annotation == null) {
                    continue;
                }
                if (Modifier.isStatic(method.getModifiers()) != statics) {
                    throw new IllegalArgumentException("Expected " + (statics ? "static" : "instance") + " @SubscribeEvent method: " + method);
                }
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != 1 || !Event.class.isAssignableFrom(parameters[0])) {
                    throw new IllegalArgumentException("@SubscribeEvent method must take exactly one Event: " + method);
                }
                addRawListener(annotation.priority(), annotation.receiveCanceled(), parameters[0], invoker(method, statics ? null : target));
                found++;
            }
            if (statics) {
                break;
            }
        }
        if (found == 0) {
            LOGGER.warn("{} has no @SubscribeEvent methods (bus {})", type.getName(), name);
        }
    }

    private static Consumer<Event> invoker(Method method, Object instance) {
        try {
            method.setAccessible(true);
            MethodHandle handle = MethodHandles.lookup().unreflect(method);
            MethodHandle bound = instance == null ? handle : handle.bindTo(instance);
            return event -> {
                try {
                    bound.invoke(event);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            };
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access " + method, e);
        }
    }

    @Override
    public <T extends Event> void addListener(Class<T> eventType, Consumer<T> listener) {
        addListener(EventPriority.NORMAL, false, eventType, listener);
    }

    @Override
    public <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> listener) {
        addRawListener(priority, receiveCanceled, eventType, listener);
    }

    @SuppressWarnings("unchecked")
    private void addRawListener(EventPriority priority, boolean receiveCanceled, Class<?> eventType, Consumer<?> listener) {
        listeners.add(new Listener(eventType, priority, receiveCanceled, (Consumer<Event>) listener, order.getAndIncrement()));
        byEventType.clear();
    }

    @Override
    public <T extends Event> T post(T event) {
        for (Listener listener : listenersFor(event.getClass())) {
            if (!listener.receiveCanceled && event instanceof ICancellableEvent cancellable && cancellable.isCanceled()) {
                continue;
            }
            listener.handler.accept(event);
        }
        return event;
    }

    @Override
    public boolean hasListeners(Class<? extends Event> eventType) {
        return listenersFor(eventType).length > 0;
    }

    private Listener[] listenersFor(Class<?> eventType) {
        return byEventType.computeIfAbsent(eventType, type -> {
            List<Listener> matching = new ArrayList<>();
            for (Listener listener : listeners) {
                if (listener.eventType.isAssignableFrom(type)) {
                    matching.add(listener);
                }
            }
            if (matching.isEmpty()) {
                return NO_LISTENERS;
            }
            matching.sort(Comparator.comparing((Listener l) -> l.priority).thenComparingLong(l -> l.order));
            return matching.toArray(Listener[]::new);
        });
    }

    @Override
    public String toString() {
        return "SimpleEventBus[" + name + "]";
    }

    private record Listener(Class<?> eventType, EventPriority priority, boolean receiveCanceled,
                            Consumer<Event> handler, long order) {
    }
}
