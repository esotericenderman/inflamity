package dev.enderman.minecraft.plugins.fire.better.events.suffocation

import org.bukkit.event.entity.EntityDamageEvent

fun EntityDamageEvent.isSuffocationDamage(): Boolean {
    return cause == EntityDamageEvent.DamageCause.SUFFOCATION
}
