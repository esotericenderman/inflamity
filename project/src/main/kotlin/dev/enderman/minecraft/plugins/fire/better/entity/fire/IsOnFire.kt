package dev.enderman.minecraft.plugins.fire.better.entity.fire

import org.bukkit.entity.Entity

fun Entity.isOnFire(): Boolean {
    return fireTicks > 0
}
