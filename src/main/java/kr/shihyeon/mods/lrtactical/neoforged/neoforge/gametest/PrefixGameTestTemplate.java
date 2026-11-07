package kr.shihyeon.mods.lrtactical.neoforged.neoforge.gametest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker only. Fabric uses @GameTest#template as written, so unprefixed templates are what
 * Fabric does anyway; the build copies them to the namespace Fabric looks them up in.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PrefixGameTestTemplate {
    boolean value() default true;
}
