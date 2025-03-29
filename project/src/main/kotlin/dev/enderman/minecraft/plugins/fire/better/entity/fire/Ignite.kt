package dev.enderman.minecraft.plugins.fire.better.entity.fire

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import org.bukkit.entity.Entity

fun Entity.combust() {
    fireTicks = FIRE_DURATION
}
