package kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.registration;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.handling.IPayloadContext;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.handling.IPayloadHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registers play payloads with Fabric networking. Client receivers are kept until the client entrypoint
 * runs, since Fabric's client networking can only be touched on the client.
 */
public class PayloadRegistrar {
    private static final List<ClientReceiver<?>> CLIENT_RECEIVERS = new ArrayList<>();

    private final String version;

    public PayloadRegistrar(String version) {
        this.version = version;
    }

    public String version() {
        return version;
    }

    public <T extends CustomPacketPayload> PayloadRegistrar playToServer(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            IPayloadHandler<T> handler) {
        PayloadTypeRegistry.playC2S().register(type, codec);
        ServerPlayNetworking.registerGlobalReceiver(type,
                (payload, context) -> handler.handle(payload, context::player));
        return this;
    }

    public <T extends CustomPacketPayload> PayloadRegistrar playToClient(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            IPayloadHandler<T> handler) {
        PayloadTypeRegistry.playS2C().register(type, codec);
        CLIENT_RECEIVERS.add(new ClientReceiver<>(type, handler));
        return this;
    }

    public static List<ClientReceiver<?>> clientReceivers() {
        return Collections.unmodifiableList(CLIENT_RECEIVERS);
    }

    public record ClientReceiver<T extends CustomPacketPayload>(CustomPacketPayload.Type<T> type, IPayloadHandler<T> handler) {
        public void handle(T payload, IPayloadContext context) {
            handler.handle(payload, context);
        }
    }
}
