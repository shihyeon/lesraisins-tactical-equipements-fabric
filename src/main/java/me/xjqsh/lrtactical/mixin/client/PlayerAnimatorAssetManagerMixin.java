package me.xjqsh.lrtactical.mixin.client;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.tacz.guns.compat.playeranimator.animation.PlayerAnimatorAssetManager;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import me.xjqsh.lrtactical.util.IPAAssetManager;
import net.minecraft.resources.ResourceLocation;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.Dist;
import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(PlayerAnimatorAssetManager.class)
public abstract class PlayerAnimatorAssetManagerMixin implements IPAAssetManager {

    @Shadow(remap = false)
    abstract Optional<KeyframeAnimation> getAnimations(ResourceLocation id, String name);

    @Unique
    @Override
    public Optional<KeyframeAnimation> lrt$getAnimations(ResourceLocation id, String name) {
        return getAnimations(id, name);
    }
}
