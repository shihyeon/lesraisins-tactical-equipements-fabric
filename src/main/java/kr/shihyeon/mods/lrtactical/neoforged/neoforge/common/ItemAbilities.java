package kr.shihyeon.mods.lrtactical.neoforged.neoforge.common;

/**
 * Only {@link #SWORD_SWEEP} has an effect on Fabric (see PlayerHooksMixin).
 */
public final class ItemAbilities {
    public static final ItemAbility SWORD_SWEEP = ItemAbility.get("sword_sweep");
    public static final ItemAbility SWORD_DIG = ItemAbility.get("sword_dig");
    public static final ItemAbility SHIELD_BLOCK = ItemAbility.get("shield_block");

    private ItemAbilities() {
    }
}
