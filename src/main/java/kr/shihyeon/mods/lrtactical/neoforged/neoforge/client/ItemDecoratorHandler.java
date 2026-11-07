package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ItemDecoratorHandler {
    private static final Map<Item, List<IItemDecorator>> DECORATORS = new IdentityHashMap<>();

    public static void register(Item item, IItemDecorator decorator) {
        DECORATORS.computeIfAbsent(item, i -> new ArrayList<>()).add(decorator);
    }

    public static void render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        List<IItemDecorator> decorators = DECORATORS.get(stack.getItem());
        if (decorators == null) {
            return;
        }
        for (IItemDecorator decorator : decorators) {
            decorator.render(guiGraphics, font, stack, xOffset, yOffset);
        }
    }

    private ItemDecoratorHandler() {
    }
}
