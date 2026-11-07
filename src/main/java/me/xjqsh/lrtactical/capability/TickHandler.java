package me.xjqsh.lrtactical.capability;

import me.xjqsh.lrtactical.EquipmentMod;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.SubscribeEvent;
import kr.shihyeon.mods.lrtactical.neoforged.fml.common.EventBusSubscriber;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = EquipmentMod.MOD_ID)
public class TickHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        CustomItemCoolDowns.get(event.getEntity()).ifPresent(CustomItemCoolDowns::tick);
        CombatProperties.get(event.getEntity()).ifPresent(CombatProperties::tick);
    }
}
