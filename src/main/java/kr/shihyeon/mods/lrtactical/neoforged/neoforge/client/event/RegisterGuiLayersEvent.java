package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

/**
 * Fabric draws mod HUD layers after the vanilla HUD in registration order, so every layer ends up on top.
 */
public class RegisterGuiLayersEvent extends Event {
    public void registerAboveAll(ResourceLocation id, LayeredDraw.Layer layer) {
        HudRenderCallback.EVENT.register(layer::render);
    }

    public void registerBelowAll(ResourceLocation id, LayeredDraw.Layer layer) {
        HudRenderCallback.EVENT.register(layer::render);
    }

    public void registerAbove(ResourceLocation other, ResourceLocation id, LayeredDraw.Layer layer) {
        HudRenderCallback.EVENT.register(layer::render);
    }

    public void registerBelow(ResourceLocation other, ResourceLocation id, LayeredDraw.Layer layer) {
        HudRenderCallback.EVENT.register(layer::render);
    }
}
