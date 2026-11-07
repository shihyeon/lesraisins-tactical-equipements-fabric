package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Items hand theirs out through {@code initializeClient}, which ClientItemExtensions collects
 * when the client starts.
 */
public interface IClientItemExtensions {
    IClientItemExtensions DEFAULT = new IClientItemExtensions() {
    };

    static IClientItemExtensions of(ItemStack stack) {
        return of(stack.getItem());
    }

    static IClientItemExtensions of(Item item) {
        return ClientItemExtensions.get(item);
    }

    @Nullable
    default HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return null;
    }

    default BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return Minecraft.getInstance().getItemRenderer().blockEntityRenderer;
    }
}
