package kr.shihyeon.mods.lrtactical.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.IKeyMappingExtension;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * Vanilla gives each key to a single mapping. NeoForge lets mappings share a key, and this mod binds its
 * attack keys to the mouse buttons that attack and use already have. This mod's mappings follow the shared
 * key without taking it away from the mapping vanilla picked.
 */
@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements IKeyMappingExtension {
    @Unique
    private static final String LRTACTICAL_CATEGORY = "key.category.lrtactical";

    @Shadow
    @Final
    private static Map<String, KeyMapping> ALL;

    @Shadow
    @Final
    private static Map<InputConstants.Key, KeyMapping> MAP;

    @Shadow
    private InputConstants.Key key;

    @Shadow
    private int clickCount;

    @Shadow
    @Final
    private String category;

    @Shadow
    public abstract void setDown(boolean value);

    @Unique
    private static boolean lrtactical$isShared(KeyMapping mapping) {
        return LRTACTICAL_CATEGORY.equals(((KeyMappingMixin) (Object) mapping).category);
    }

    @Inject(method = "click", at = @At("TAIL"))
    private static void lrtactical$clickSharedMappings(InputConstants.Key key, CallbackInfo ci) {
        KeyMapping owner = MAP.get(key);
        for (KeyMapping mapping : ALL.values()) {
            KeyMappingMixin self = (KeyMappingMixin) (Object) mapping;
            if (mapping != owner && lrtactical$isShared(mapping) && self.key.equals(key)) {
                self.clickCount++;
            }
        }
    }

    @Inject(method = "set", at = @At("TAIL"))
    private static void lrtactical$setSharedMappings(InputConstants.Key key, boolean held, CallbackInfo ci) {
        KeyMapping owner = MAP.get(key);
        for (KeyMapping mapping : ALL.values()) {
            KeyMappingMixin self = (KeyMappingMixin) (Object) mapping;
            if (mapping != owner && lrtactical$isShared(mapping) && self.key.equals(key)) {
                self.setDown(held);
            }
        }
    }

    // Hand a shared key back to another mapping, so attack and use keep working
    @Inject(method = "resetMapping", at = @At("TAIL"))
    private static void lrtactical$keepVanillaOwner(CallbackInfo ci) {
        for (Map.Entry<InputConstants.Key, KeyMapping> entry : MAP.entrySet()) {
            if (!lrtactical$isShared(entry.getValue())) {
                continue;
            }
            for (KeyMapping mapping : ALL.values()) {
                if (!lrtactical$isShared(mapping) && ((KeyMappingMixin) (Object) mapping).key.equals(entry.getKey())) {
                    entry.setValue(mapping);
                    break;
                }
            }
        }
    }
}
