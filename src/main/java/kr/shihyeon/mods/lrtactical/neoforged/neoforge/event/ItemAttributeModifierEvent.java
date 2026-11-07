package kr.shihyeon.mods.lrtactical.neoforged.neoforge.event;

import kr.shihyeon.mods.lrtactical.neoforged.bus.api.Event;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemAttributeModifierEvent extends Event {
    private final ItemStack stack;
    private final ItemAttributeModifiers defaultModifiers;
    private List<ItemAttributeModifiers.Entry> entries;

    public ItemAttributeModifierEvent(ItemStack stack, ItemAttributeModifiers defaultModifiers) {
        this.stack = stack;
        this.defaultModifiers = defaultModifiers;
    }

    public ItemStack getItemStack() {
        return stack;
    }

    public ItemAttributeModifiers getDefaultModifiers() {
        return defaultModifiers;
    }

    public List<ItemAttributeModifiers.Entry> getModifiers() {
        return entries == null ? defaultModifiers.modifiers() : List.copyOf(entries);
    }

    private List<ItemAttributeModifiers.Entry> entries() {
        if (entries == null) {
            entries = new ArrayList<>(defaultModifiers.modifiers());
        }
        return entries;
    }

    private int indexOf(Holder<Attribute> attribute, ResourceLocation id) {
        List<ItemAttributeModifiers.Entry> list = entries();
        for (int i = 0; i < list.size(); i++) {
            ItemAttributeModifiers.Entry entry = list.get(i);
            if (entry.attribute().equals(attribute) && entry.modifier().id().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public boolean addModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
        if (indexOf(attribute, modifier.id()) >= 0) {
            return false;
        }
        entries().add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
        return true;
    }

    public void replaceModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
        ItemAttributeModifiers.Entry entry = new ItemAttributeModifiers.Entry(attribute, modifier, slot);
        int index = indexOf(attribute, modifier.id());
        if (index >= 0) {
            entries().set(index, entry);
        } else {
            entries().add(entry);
        }
    }

    public boolean removeModifier(Holder<Attribute> attribute, ResourceLocation id) {
        int index = indexOf(attribute, id);
        if (index < 0) {
            return false;
        }
        entries().remove(index);
        return true;
    }

    public boolean removeIf(Predicate<ItemAttributeModifiers.Entry> condition) {
        return entries().removeIf(condition);
    }

    public void clearModifiers() {
        entries().clear();
    }

    public ItemAttributeModifiers build() {
        return entries == null ? defaultModifiers : new ItemAttributeModifiers(List.copyOf(entries), defaultModifiers.showInTooltip());
    }
}
