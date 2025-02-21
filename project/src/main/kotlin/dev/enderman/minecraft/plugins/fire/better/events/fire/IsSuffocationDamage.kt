package dev.enderman.minecraft.plugins.fire.better.events.fire

import org.bukkit.event.entity.EntityDamageEvent

fun EntityDamageEvent.isSuffocationDamage(): Boolean {
    return cause == EntityDamageEvent.DamageCause.SUFFOCATION
}
