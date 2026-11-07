package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

import javax.annotation.Nullable;

/**
 * A null sound stops it from playing.
 */
public class PlaySoundEvent extends SoundEvent {
    private final String name;
    private final SoundInstance originalSound;
    @Nullable
    private SoundInstance sound;

    public PlaySoundEvent(SoundEngine engine, SoundInstance sound) {
        super(engine);
        this.originalSound = sound;
        this.name = sound.getLocation().getPath();
        this.sound = sound;
    }

    public String getName() {
        return name;
    }

    public SoundInstance getOriginalSound() {
        return originalSound;
    }

    @Nullable
    public SoundInstance getSound() {
        return sound;
    }

    public void setSound(@Nullable SoundInstance newSound) {
        this.sound = newSound;
    }
}
