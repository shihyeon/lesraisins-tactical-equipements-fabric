package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.function.Consumer;

public final class AttributeUtil {
    /**
     * Same lines as vanilla PotionContents#addPotionTooltip.
     */
    public static void addPotionTooltip(List<Pair<Holder<Attribute>, AttributeModifier>> list, Consumer<Component> tooltips) {
        for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
            AttributeModifier modifier = pair.getSecond();
            double amount = modifier.amount();
            double shown = modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    ? amount * 100.0 : amount;
            Component name = Component.translatable(pair.getFirst().value().getDescriptionId());
            if (amount > 0.0) {
                tooltips.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(),
                        ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(shown), name).withStyle(ChatFormatting.BLUE));
            } else if (amount < 0.0) {
                tooltips.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(),
                        ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-shown), name).withStyle(ChatFormatting.RED));
            }
        }
    }

    private AttributeUtil() {
    }
}
