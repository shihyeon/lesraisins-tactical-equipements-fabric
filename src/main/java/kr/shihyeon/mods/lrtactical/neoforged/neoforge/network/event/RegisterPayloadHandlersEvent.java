package kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.registration.PayloadRegistrar;

public class RegisterPayloadHandlersEvent extends Event {
    public PayloadRegistrar registrar(String version) {
        return new PayloadRegistrar(version);
    }
}
