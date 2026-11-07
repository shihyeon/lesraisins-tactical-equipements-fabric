package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

/*
 * Injected into KeyMapping with Loom interface injection (see fabric.mod.json) and added at runtime by
 * KeyMappingMixin, so upstream code that overrides or calls these NeoForge methods compiles unchanged.
 */
public interface IKeyMappingExtension {
    default boolean isActiveAndMatches(InputConstants.Key keyCode) {
        return keyCode != InputConstants.UNKNOWN && keyCode.equals(KeyBindingHelper.getBoundKeyOf((KeyMapping) this));
    }
}
