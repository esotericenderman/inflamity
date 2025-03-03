package dev.enderman.minecraft.plugins.fire.better.entity.fire

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import org.bukkit.entity.Entity

fun Entity.ignite() {
    fireTicks = FIRE_DURATION
}
