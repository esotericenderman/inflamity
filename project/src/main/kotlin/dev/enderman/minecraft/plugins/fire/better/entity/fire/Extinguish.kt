package dev.enderman.minecraft.plugins.fire.better.entity.fire

import org.bukkit.entity.Entity

fun Entity.extinguish() {
    fireTicks = 0
}
