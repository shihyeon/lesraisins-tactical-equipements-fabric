package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.gui;

import kr.shihyeon.mods.lrtactical.neoforged.fml.IExtensionPoint;
import kr.shihyeon.mods.lrtactical.neoforged.fml.ModContainer;
import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface IConfigScreenFactory extends IExtensionPoint {
    Screen createScreen(ModContainer container, Screen modListScreen);
}
