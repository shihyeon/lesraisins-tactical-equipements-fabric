package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.extensions.common;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.common.extensions.IItemExtension;
import net.minecraft.world.item.Item;

import java.util.IdentityHashMap;
import java.util.Map;

public final class ClientItemExtensions {
    private static final Map<Item, IClientItemExtensions> EXTENSIONS = new IdentityHashMap<>();

    public static void init(Item item) {
        if (EXTENSIONS.containsKey(item)) {
            return;
        }
        ((IItemExtension) item).initializeClient(extension -> {
            if (EXTENSIONS.putIfAbsent(item, extension) != null) {
                throw new IllegalStateException("Duplicate client item extensions for " + item);
            }
        });
    }

    public static IClientItemExtensions get(Item item) {
        return EXTENSIONS.getOrDefault(item, IClientItemExtensions.DEFAULT);
    }

    public static boolean has(Item item) {
        return EXTENSIONS.containsKey(item);
    }

    private ClientItemExtensions() {
    }
}
