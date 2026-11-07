package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.IItemDecorator;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.ItemDecoratorHandler;
import net.minecraft.world.level.ItemLike;

public class RegisterItemDecorationsEvent extends Event {
    public void register(ItemLike itemLike, IItemDecorator decorator) {
        ItemDecoratorHandler.register(itemLike.asItem(), decorator);
    }
}
