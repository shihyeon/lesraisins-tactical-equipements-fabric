package kr.shihyeon.mods.lrtactical.neoforged.fml.common;

import kr.shihyeon.mods.lrtactical.neoforged.api.distmarker.Dist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes with this annotation are found by {@code EventSubscriberScanner} at startup and their static
 * {@code @SubscribeEvent} methods are registered, like on NeoForge.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber {
    Dist[] value() default {Dist.CLIENT, Dist.DEDICATED_SERVER};

    String modid() default "";

    Bus bus() default Bus.GAME;

    enum Bus {
        GAME,
        MOD
    }
}
