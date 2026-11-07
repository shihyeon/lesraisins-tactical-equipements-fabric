package kr.shihyeon.mods.lrtactical.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
    @ModifyReturnValue(method = "getArmPose", at = @At("RETURN"))
    private static HumanoidModel.ArmPose lrtactical$itemArmPose(HumanoidModel.ArmPose original,
                                                                @Local(argsOnly = true) AbstractClientPlayer player,
                                                                @Local(argsOnly = true) InteractionHand hand) {
        if (original != HumanoidModel.ArmPose.ITEM) {
            return original;
        }
        ItemStack stack = player.getItemInHand(hand);
        HumanoidModel.ArmPose pose = IClientItemExtensions.of(stack).getArmPose(player, hand, stack);
        return pose != null ? pose : original;
    }
}
