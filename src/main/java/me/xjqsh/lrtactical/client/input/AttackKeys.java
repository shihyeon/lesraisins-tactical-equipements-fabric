package me.xjqsh.lrtactical.client.input;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.mojang.blaze3d.platform.InputConstants;
import com.tacz.guns.client.renderer.item.AnimateGeoItemRenderer;
import me.xjqsh.lrtactical.api.melee.MeleeAction;
import me.xjqsh.lrtactical.capability.CombatProperties;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.Dist;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.OnlyIn;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.InputEvent;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.settings.KeyConflictContext;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.settings.KeyModifier;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event.ClientTickEvent;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.SubscribeEvent;
import kr.shihyeon.mods.lrtactical.neoforged.fml.common.EventBusSubscriber;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class AttackKeys {
    public static final KeyMapping NORMAL_ATTACK = new KeyMapping("key.lrtactical.normal_attack.desc",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_LEFT,
            "key.category.lrtactical");

    public static final KeyMapping SPECIAL_ATTACK = new KeyMapping("key.lrtactical.sp_attack.desc",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_RIGHT,
            "key.category.lrtactical");

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {

        var mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || player.isSpectator() || mc.gameMode == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();

        if (NORMAL_ATTACK.isDown()) {
            CombatProperties.get(player).ifPresent(combatProperties -> {
                if (combatProperties.getCoolDownTick() > 0) return;

                if (combatProperties.preAttack(MeleeAction.LEFT, player.getEyePosition(), player.getLookAngle())) {
                    mc.gameMode.ensureHasSentCarriedItem();
                    if (IClientItemExtensions.of(stack).getCustomRenderer() instanceof AnimateGeoItemRenderer<?, ?> renderer) {
                        renderer.triggerAnimation(stack, "attack_left");
                        player.swing(InteractionHand.MAIN_HAND);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onNormalAttack(InputEvent.MouseButton.Post event) {
        var mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || player.isSpectator() || mc.gameMode == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();

        while (NORMAL_ATTACK.consumeClick()) {
            CombatProperties.get(player).ifPresent(combatProperties -> {
                if (combatProperties.preAttack(MeleeAction.LEFT, player.getEyePosition(), player.getLookAngle())) {
                    mc.gameMode.ensureHasSentCarriedItem();
                    if (IClientItemExtensions.of(stack).getCustomRenderer() instanceof AnimateGeoItemRenderer<?, ?> renderer) {
                        renderer.triggerAnimation(stack, "attack_left");
                        player.swing(InteractionHand.MAIN_HAND);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onSpAttack(InputEvent.MouseButton.Post event) {
        var mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || player.isSpectator() || mc.gameMode == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();

        while (SPECIAL_ATTACK.consumeClick()) {
            CombatProperties.get(player).ifPresent(combatProperties -> {
                if (combatProperties.preAttack(MeleeAction.RIGHT, player.getEyePosition(), player.getLookAngle())) {
                    mc.gameMode.ensureHasSentCarriedItem();
                    if (IClientItemExtensions.of(stack).getCustomRenderer() instanceof AnimateGeoItemRenderer<?, ?> renderer) {
                        renderer.triggerAnimation(stack, "attack_right");
                        player.swing(InteractionHand.MAIN_HAND);
                    }
                }
            });
        }

    }
}
