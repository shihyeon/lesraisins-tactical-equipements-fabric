package me.xjqsh.lrtactical.network.message;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.tacz.guns.client.renderer.item.AnimateGeoItemRenderer;
import me.xjqsh.lrtactical.item.FlashShieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.Dist;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.OnlyIn;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;

public record SShieldDisable() implements CustomPacketPayload {
    public static final Type<SShieldDisable> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("lrtactical", "s_shield_disable"));
    public static final StreamCodec<FriendlyByteBuf, SShieldDisable> STREAM_CODEC =
            StreamCodec.of((buf, message) -> encode(message, buf), SShieldDisable::decode);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(SShieldDisable message, FriendlyByteBuf buf) {
    }

    public static SShieldDisable decode(FriendlyByteBuf buf) {
        return new SShieldDisable();
    }

    public static void handle(SShieldDisable message, IPayloadContext context) {
        context.enqueueWork(() -> handle(message));
    }

    @Environment(EnvType.CLIENT)
    private static void handle(SShieldDisable message) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        if (player == null || level == null) {
            return;
        }
        if (player.getMainHandItem().getItem() instanceof FlashShieldItem) {
            if (IClientItemExtensions.of(player.getMainHandItem()).getCustomRenderer() instanceof AnimateGeoItemRenderer<?,?> renderer) {
                renderer.triggerAnimation(player.getMainHandItem(), "hard_shake");
            }
        }
    }
}
