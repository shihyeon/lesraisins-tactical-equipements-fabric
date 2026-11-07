package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterClientReloadListenersEvent extends Event {
    private final List<PreparableReloadListener> listeners = new ArrayList<>();

    public void registerReloadListener(PreparableReloadListener reloadListener) {
        listeners.add(reloadListener);
    }

    public List<PreparableReloadListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }
}
