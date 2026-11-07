package kr.shihyeon.mods.lrtactical.neoforged.neoforge.attachment;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Only non-serialized attachments: values live on the holder until it is discarded and are not
 * copied on death, as on NeoForge.
 */
public final class AttachmentType<T> {
    private final Function<IAttachmentHolder, T> defaultValueConstructor;

    private AttachmentType(Function<IAttachmentHolder, T> defaultValueConstructor) {
        this.defaultValueConstructor = defaultValueConstructor;
    }

    public static <T> Builder<T> builder(Function<IAttachmentHolder, T> defaultValueConstructor) {
        return new Builder<>(defaultValueConstructor);
    }

    public static <T> Builder<T> builder(Supplier<T> defaultValueSupplier) {
        return new Builder<>(holder -> defaultValueSupplier.get());
    }

    T createDefault(IAttachmentHolder holder) {
        return defaultValueConstructor.apply(holder);
    }

    public static final class Builder<T> {
        private final Function<IAttachmentHolder, T> defaultValueConstructor;

        private Builder(Function<IAttachmentHolder, T> defaultValueConstructor) {
            this.defaultValueConstructor = defaultValueConstructor;
        }

        public AttachmentType<T> build() {
            return new AttachmentType<>(defaultValueConstructor);
        }
    }
}
