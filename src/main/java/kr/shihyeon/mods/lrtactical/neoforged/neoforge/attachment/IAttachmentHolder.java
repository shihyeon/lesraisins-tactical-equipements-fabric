package kr.shihyeon.mods.lrtactical.neoforged.neoforge.attachment;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Injected into {@code Entity} through Loom interface injection; the storage comes from EntityAttachmentMixin.
 */
public interface IAttachmentHolder {
    /**
     * Provided by EntityAttachmentMixin; the default only exists so subclasses compile.
     */
    default Map<AttachmentType<?>, Object> lrtactical$getAttachments() {
        throw new IllegalStateException("EntityAttachmentMixin was not applied to " + getClass().getName());
    }

    default boolean hasData(AttachmentType<?> type) {
        return lrtactical$getAttachments().containsKey(type);
    }

    default <T> boolean hasData(Supplier<AttachmentType<T>> type) {
        return hasData(type.get());
    }

    @SuppressWarnings("unchecked")
    default <T> T getData(AttachmentType<T> type) {
        Map<AttachmentType<?>, Object> attachments = lrtactical$getAttachments();
        Object value = attachments.get(type);
        if (value == null) {
            value = type.createDefault(this);
            attachments.put(type, value);
        }
        return (T) value;
    }

    default <T> T getData(Supplier<AttachmentType<T>> type) {
        return getData(type.get());
    }

    @SuppressWarnings("unchecked")
    default <T> T setData(AttachmentType<T> type, T data) {
        return (T) lrtactical$getAttachments().put(type, data);
    }

    default <T> T setData(Supplier<AttachmentType<T>> type, T data) {
        return setData(type.get(), data);
    }

    @SuppressWarnings("unchecked")
    default <T> T removeData(AttachmentType<T> type) {
        return (T) lrtactical$getAttachments().remove(type);
    }
}
