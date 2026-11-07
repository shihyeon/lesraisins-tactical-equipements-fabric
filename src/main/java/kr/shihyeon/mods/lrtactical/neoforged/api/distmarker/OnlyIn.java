package kr.shihyeon.mods.lrtactical.neoforged.api.distmarker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Kept for source compatibility. The portUpstream Gradle task rewrites upstream uses to Fabric's
 * {@code @Environment}, which Fabric Loader strips on the other side like NeoForge does with this.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PACKAGE})
public @interface OnlyIn {
    Dist value();
}
