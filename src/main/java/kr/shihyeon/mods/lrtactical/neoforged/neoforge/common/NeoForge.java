package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.IEventBus;
import kr.shihyeon.mods.lrtactical.event.SimpleEventBus;

public final class NeoForge {
    public static final IEventBus EVENT_BUS = new SimpleEventBus("game");

    private NeoForge() {
    }
}
