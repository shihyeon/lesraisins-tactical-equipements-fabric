package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddReloadListenerEvent extends Event {
    private final List<PreparableReloadListener> listeners = new ArrayList<>();
    private final ReloadableServerResources serverResources;
    private final RegistryAccess registryAccess;

    public AddReloadListenerEvent(ReloadableServerResources serverResources, RegistryAccess registryAccess) {
        this.serverResources = serverResources;
        this.registryAccess = registryAccess;
    }

    public void addListener(PreparableReloadListener listener) {
        listeners.add(listener);
    }

    public List<PreparableReloadListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public ReloadableServerResources getServerResources() {
        return serverResources;
    }

    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }
}
