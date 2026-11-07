package kr.shihyeon.mods.lrtactical;

import kr.shihyeon.mods.lrtactical.event.ClientEventBridge;
import kr.shihyeon.mods.lrtactical.event.ClientReloadListeners;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.IEventBus;
import kr.shihyeon.mods.lrtactical.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.EntityRenderersEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common.ClientItemExtensions;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public class LrTacticalFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IEventBus modBus = LrTacticalFabric.getModBus();

        registerItemRenderers();
        PayloadRegistrar.clientReceivers().forEach(LrTacticalFabricClient::registerReceiver);

        modBus.post(new RegisterKeyMappingsEvent());
        modBus.post(new EntityRenderersEvent.RegisterRenderers());
        modBus.post(new RegisterParticleProvidersEvent());
        modBus.post(new RegisterItemDecorationsEvent());
        modBus.post(new RegisterClientTooltipComponentFactoriesEvent());
        modBus.post(new RegisterGuiLayersEvent());
        ClientReloadListeners.register(modBus.post(new RegisterClientReloadListenersEvent()).getListeners());
        modBus.post(new FMLClientSetupEvent());

        ClientEventBridge.register();
    }

    /**
     * NeoForge renders items through IClientItemExtensions#getCustomRenderer. TACZ looks renderers up in
     * Fabric's registry and checks their type, so the renderer itself is registered when it can be.
     */
    private static void registerItemRenderers() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (!LrTacticalFabric.MOD_ID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace())) {
                continue;
            }
            ClientItemExtensions.init(item);
            if (!ClientItemExtensions.has(item)) {
                continue;
            }
            IClientItemExtensions extensions = IClientItemExtensions.of(item);
            BlockEntityWithoutLevelRenderer renderer = extensions.getCustomRenderer();
            if (renderer instanceof BuiltinItemRendererRegistry.DynamicItemRenderer dynamic) {
                BuiltinItemRendererRegistry.INSTANCE.register(item, dynamic);
            } else {
                BuiltinItemRendererRegistry.INSTANCE.register(item, renderer::renderByItem);
            }
        }
    }

    private static <T extends CustomPacketPayload> void registerReceiver(PayloadRegistrar.ClientReceiver<T> receiver) {
        ClientPlayNetworking.registerGlobalReceiver(receiver.type(),
                (payload, context) -> receiver.handle(payload, context::player));
    }
}
