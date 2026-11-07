package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.function.Function;

public class RegisterClientTooltipComponentFactoriesEvent extends Event {
    public <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {
        TooltipComponentCallback.EVENT.register(data -> type.isInstance(data) ? factory.apply(type.cast(data)) : null);
    }
}
