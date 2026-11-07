package kr.shihyeon.mods.lrtactical;

import kr.shihyeon.mods.lrtactical.event.CommonEventBridge;
import kr.shihyeon.mods.lrtactical.event.EventSubscriberScanner;
import kr.shihyeon.mods.lrtactical.event.SimpleEventBus;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.IEventBus;
import kr.shihyeon.mods.lrtactical.neoforged.fml.ModContainer;
import kr.shihyeon.mods.lrtactical.neoforged.fml.ModList;
import kr.shihyeon.mods.lrtactical.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.NeoForge;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries.NewRegistryEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.registries.RegisterEvent;
import me.xjqsh.lrtactical.EquipmentMod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

public class LrTacticalFabric implements ModInitializer {
    public static final String MOD_ID = EquipmentMod.MOD_ID;
    public static final String UPSTREAM_PACKAGE = "me.xjqsh.lrtactical";

    private static final IEventBus MOD_BUS = new SimpleEventBus("mod");
    @Nullable
    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        var fabricMod = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
        ModContainer container = ModList.get().getModContainerById(MOD_ID).orElseThrow();

        EventSubscriberScanner.registerAll(fabricMod, UPSTREAM_PACKAGE, MOD_BUS, NeoForge.EVENT_BUS);
        new EquipmentMod(MOD_BUS, container);

        MOD_BUS.post(new NewRegistryEvent());
        MOD_BUS.post(new RegisterEvent());
        MOD_BUS.post(new RegisterPayloadHandlersEvent());
        MOD_BUS.post(new FMLCommonSetupEvent());

        CommonEventBridge.register();
    }

    public static IEventBus getModBus() {
        return MOD_BUS;
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(@Nullable MinecraftServer current) {
        server = current;
    }
}
