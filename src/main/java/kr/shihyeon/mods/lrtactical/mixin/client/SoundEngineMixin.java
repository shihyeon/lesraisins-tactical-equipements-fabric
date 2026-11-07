package kr.shihyeon.mods.lrtactical.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Shadow
    private boolean loaded;

    @WrapMethod(method = "play")
    private void lrtactical$playSoundEvent(SoundInstance sound, Operation<Void> original) {
        if (!this.loaded || !NeoForge.EVENT_BUS.hasListeners(PlaySoundEvent.class)) {
            original.call(sound);
            return;
        }
        SoundInstance result = NeoForge.EVENT_BUS.post(new PlaySoundEvent((SoundEngine) (Object) this, sound)).getSound();
        if (result != null) {
            original.call(result);
        }
    }
}
