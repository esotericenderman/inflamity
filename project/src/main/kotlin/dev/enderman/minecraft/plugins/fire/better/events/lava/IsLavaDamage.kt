package dev.enderman.minecraft.plugins.fire.better.events.lava

import org.bukkit.event.entity.EntityDamageEvent

fun EntityDamageEvent.isLavaDamage(): Boolean {
    return cause == EntityDamageEvent.DamageCause.LAVA
}
