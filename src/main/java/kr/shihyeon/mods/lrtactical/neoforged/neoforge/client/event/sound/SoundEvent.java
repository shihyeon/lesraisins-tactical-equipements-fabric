package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.sound;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.client.sounds.SoundEngine;

public abstract class SoundEvent extends Event {
    private final SoundEngine engine;

    protected SoundEvent(SoundEngine engine) {
        this.engine = engine;
    }

    public SoundEngine getEngine() {
        return engine;
    }
}
