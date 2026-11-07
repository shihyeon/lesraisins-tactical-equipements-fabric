package kr.shihyeon.mods.lrtactical.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;

/**
 * Never posted: the only upstream listener has its body commented out.
 */
public class RenderLevelStageEvent extends Event {
    private final Stage stage;
    private final LevelRenderer levelRenderer;
    private final PoseStack poseStack;
    private final DeltaTracker partialTick;
    private final Camera camera;

    public RenderLevelStageEvent(Stage stage, LevelRenderer levelRenderer, PoseStack poseStack, DeltaTracker partialTick, Camera camera) {
        this.stage = stage;
        this.levelRenderer = levelRenderer;
        this.poseStack = poseStack;
        this.partialTick = partialTick;
        this.camera = camera;
    }

    public Stage getStage() {
        return stage;
    }

    public LevelRenderer getLevelRenderer() {
        return levelRenderer;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public DeltaTracker getPartialTick() {
        return partialTick;
    }

    public Camera getCamera() {
        return camera;
    }

    public static final class Stage {
        public static final Stage AFTER_SKY = new Stage("after_sky");
        public static final Stage AFTER_SOLID_BLOCKS = new Stage("after_solid_blocks");
        public static final Stage AFTER_ENTITIES = new Stage("after_entities");
        public static final Stage AFTER_BLOCK_ENTITIES = new Stage("after_block_entities");
        public static final Stage AFTER_TRANSLUCENT_BLOCKS = new Stage("after_translucent_blocks");
        public static final Stage AFTER_PARTICLES = new Stage("after_particles");
        public static final Stage AFTER_WEATHER = new Stage("after_weather");
        public static final Stage AFTER_LEVEL = new Stage("after_level");

        private final String name;

        private Stage(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
