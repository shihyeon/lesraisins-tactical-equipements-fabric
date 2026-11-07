package kr.shihyeon.mods.lrtactical.neoforged.neoforge.gametest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker only. On Fabric, test classes are listed under the fabric-gametest entrypoint of the
 * lrtactical-gametest mod (src/gameTest/resources/fabric.mod.json).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GameTestHolder {
    String value() default "minecraft";
}
