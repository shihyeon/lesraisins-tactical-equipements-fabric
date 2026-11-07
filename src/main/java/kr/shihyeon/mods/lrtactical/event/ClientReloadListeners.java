package kr.shihyeon.mods.lrtactical.event;

import kr.shihyeon.mods.lrtactical.LrTacticalFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Hands the listeners from RegisterClientReloadListenersEvent to Fabric. They run after TACZ's client
 * listeners, as the upstream handler asks for with EventPriority.LOW.
 */
@Environment(EnvType.CLIENT)
public final class ClientReloadListeners {
    private static final Set<ResourceLocation> AFTER = Set.of(
            ResourceLocation.fromNamespaceAndPath("tacz", "client_index_manager_reload"),
            ResourceLocation.fromNamespaceAndPath("tacz", "sound_manager")
    );

    public static void register(List<PreparableReloadListener> listeners) {
        ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        for (int i = 0; i < listeners.size(); i++) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LrTacticalFabric.MOD_ID, "client_reload_listener_" + i);
            helper.registerReloadListener(new Wrapped(id, listeners.get(i)));
        }
    }

    private record Wrapped(ResourceLocation id, PreparableReloadListener delegate) implements IdentifiableResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return id;
        }

        @Override
        public Collection<ResourceLocation> getFabricDependencies() {
            return AFTER;
        }

        @Override
        public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationsProfiler,
                                              ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return delegate.reload(barrier, manager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
        }

        @Override
        public String getName() {
            return delegate.getName();
        }
    }

    private ClientReloadListeners() {
    }
}
