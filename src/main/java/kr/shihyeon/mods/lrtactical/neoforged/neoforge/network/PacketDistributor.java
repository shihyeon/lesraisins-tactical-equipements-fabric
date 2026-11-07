package kr.shihyeon.mods.lrtactical.neoforged.neoforge.network;

import kr.shihyeon.mods.lrtactical.LrTacticalFabric;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public final class PacketDistributor {
    public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        Client.send(payload, payloads);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        send(player, payload, payloads);
    }

    public static void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        for (ServerPlayer player : PlayerLookup.world(level)) {
            send(player, payload, payloads);
        }
    }

    public static void sendToPlayersNear(ServerLevel level, @Nullable ServerPlayer excluded, double x, double y, double z,
                                         double radius, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        double radiusSqr = radius * radius;
        for (ServerPlayer player : PlayerLookup.world(level)) {
            if (player != excluded && player.distanceToSqr(x, y, z) < radiusSqr) {
                send(player, payload, payloads);
            }
        }
    }

    public static void sendToAllPlayers(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        MinecraftServer server = LrTacticalFabric.getServer();
        if (server == null) {
            return;
        }
        for (ServerPlayer player : PlayerLookup.all(server)) {
            send(player, payload, payloads);
        }
    }

    public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            send(player, payload, payloads);
        }
    }

    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        sendToPlayersTrackingEntity(entity, payload, payloads);
        if (entity instanceof ServerPlayer player) {
            send(player, payload, payloads);
        }
    }

    private static void send(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        ServerPlayNetworking.send(player, payload);
        for (CustomPacketPayload extra : payloads) {
            ServerPlayNetworking.send(player, extra);
        }
    }

    // Kept in its own class so the dedicated server never loads Fabric's client networking
    private static final class Client {
        static void send(CustomPacketPayload payload, CustomPacketPayload... payloads) {
            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(payload);
            for (CustomPacketPayload extra : payloads) {
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(extra);
            }
        }
    }

    private PacketDistributor() {
    }
}
