package kr.shihyeon.mods.lrtactical.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.ICancellableEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.ClientTickEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.InputEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RenderFrameEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RenderHandEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.ViewportEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public final class ClientEventBridge {
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> NeoForge.EVENT_BUS.post(new ClientTickEvent.Pre()));
        ClientTickEvents.END_CLIENT_TICK.register(client -> NeoForge.EVENT_BUS.post(new ClientTickEvent.Post()));

        cn.sh1rocu.tacz.api.event.InputEvent.Key.EVENT.register(e ->
                NeoForge.EVENT_BUS.post(new InputEvent.Key(e.getKey(), e.getScanCode(), e.getAction(), e.getModifiers())));
        cn.sh1rocu.tacz.api.event.InputEvent.MouseButton.Pre.EVENT.register(e -> {
            var event = NeoForge.EVENT_BUS.post(new InputEvent.MouseButton.Pre(e.getButton(), e.getAction(), e.getModifiers()));
            if (event.isCanceled()) {
                e.setCanceled(true);
            }
        });
        cn.sh1rocu.tacz.api.event.InputEvent.MouseButton.Post.EVENT.register(e ->
                NeoForge.EVENT_BUS.post(new InputEvent.MouseButton.Post(e.getButton(), e.getAction(), e.getModifiers())));
        cn.sh1rocu.tacz.api.event.InputEvent.InteractionKeyMappingTriggered.EVENT.register(e -> {
            if (e.isCanceled()) {
                return;
            }
            var event = new InputEvent.InteractionKeyMappingTriggered(e.isAttack() ? 0 : e.isUseItem() ? 1 : 2, e.getKeyMapping(), e.getHand());
            event.setSwingHand(e.shouldSwingHand());
            NeoForge.EVENT_BUS.post(event);
            e.setSwingHand(event.shouldSwingHand());
            if (event.isCanceled()) {
                e.setCanceled(true);
            }
        });

        cn.sh1rocu.simplebedrockmodel.api.event.ViewportEvent.CAMERA.register(e -> {
            var event = NeoForge.EVENT_BUS.post(new ViewportEvent.ComputeCameraAngles(e.getRenderer(), e.getCamera(), e.getPartialTick(),
                    e.getYaw(), e.getPitch(), e.getRoll()));
            e.setYaw(event.getYaw());
            e.setPitch(event.getPitch());
            e.setRoll(event.getRoll());
        });
        cn.sh1rocu.simplebedrockmodel.api.event.RenderHandEvent.EVENT.register(e -> {
            if (e.isCanceled()) {
                return;
            }
            var event = NeoForge.EVENT_BUS.post(new RenderHandEvent(e.getHand(), e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight(),
                    e.getPartialTick(), e.getPitch(), e.getSwingProgress(), e.getEquipProgress(), e.getItemStack()));
            if (((ICancellableEvent) event).isCanceled()) {
                e.setCanceled(true);
            }
        });
        cn.sh1rocu.simplebedrockmodel.api.event.RenderFrameEvent.EVENT.register(e -> {
            if (e.phase == cn.sh1rocu.simplebedrockmodel.api.event.RenderFrameEvent.Phase.START) {
                NeoForge.EVENT_BUS.post(new RenderFrameEvent.Pre(e.getTimer()));
            } else {
                NeoForge.EVENT_BUS.post(new RenderFrameEvent.Post(e.getTimer()));
            }
        });
    }

    private ClientEventBridge() {
    }
}
