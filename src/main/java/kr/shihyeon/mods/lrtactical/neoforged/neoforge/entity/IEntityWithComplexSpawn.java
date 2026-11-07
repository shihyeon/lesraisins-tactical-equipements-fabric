package kr.shihyeon.mods.lrtactical.neoforged.neoforge.entity;

import cn.sh1rocu.tacz.util.neoforge.network.IEntityExtension;

/**
 * TACZ-Refabricated already sends spawn data for entities with its port of this interface,
 * so this simply joins both of its interfaces.
 */
public interface IEntityWithComplexSpawn extends cn.sh1rocu.tacz.util.neoforge.network.IEntityWithComplexSpawn, IEntityExtension {
}
