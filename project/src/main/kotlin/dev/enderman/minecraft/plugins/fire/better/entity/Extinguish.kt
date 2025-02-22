package dev.enderman.minecraft.plugins.fire.better.entity

import org.bukkit.entity.Entity

fun Entity.extinguish() {
    fireTicks = 0
}
