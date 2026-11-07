package kr.shihyeon.mods.lrtactical.mixin.extension;

import kr.shihyeon.mods.lrtactical.neoforged.neoforge.attachment.AttachmentType;
import kr.shihyeon.mods.lrtactical.neoforged.neoforge.attachment.IAttachmentHolder;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityAttachmentMixin implements IAttachmentHolder {
    @Unique
    private Map<AttachmentType<?>, Object> lrtactical$attachments;

    @Override
    public Map<AttachmentType<?>, Object> lrtactical$getAttachments() {
        if (lrtactical$attachments == null) {
            lrtactical$attachments = new IdentityHashMap<>();
        }
        return lrtactical$attachments;
    }
}
