package kr.shihyeon.mods.lrtactical.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import kr.shihyeon.mods.lrtactical.LrTacticalFabric;
import kr.shihyeon.mods.lrtactical.neoforged.fml.ModContainer;
import kr.shihyeon.mods.lrtactical.neoforged.fml.ModList;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModContainer container = ModList.get().getModContainerById(LrTacticalFabric.MOD_ID).orElseThrow();
            return container.getCustomExtension(IConfigScreenFactory.class)
                    .map(factory -> factory.createScreen(container, parent))
                    .orElse(null);
        };
    }
}
