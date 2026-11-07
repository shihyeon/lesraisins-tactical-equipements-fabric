package kr.shihyeon.mods.lrtactical.event;

import kr.shihyeon.mods.lrtactical.LrTacticalFabric;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.AddReloadListenerEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.OnDatapackSyncEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class CommonEventBridge {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(LrTacticalFabric::setServer);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            NeoForge.EVENT_BUS.post(new ServerStoppedEvent(server));
            LrTacticalFabric.setServer(null);
        });

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) ->
                NeoForge.EVENT_BUS.post(new OnDatapackSyncEvent(player.server.getPlayerList(), player)));

        cn.sh1rocu.tacz.api.event.AddReloadListenerEvent.CALLBACK.register(taczEvent -> {
            AddReloadListenerEvent event = NeoForge.EVENT_BUS.post(
                    new AddReloadListenerEvent(taczEvent.getServerResources(), taczEvent.getRegistryAccess()));
            event.getListeners().forEach(taczEvent::addListener);
        });
    }

    private CommonEventBridge() {
    }
}
