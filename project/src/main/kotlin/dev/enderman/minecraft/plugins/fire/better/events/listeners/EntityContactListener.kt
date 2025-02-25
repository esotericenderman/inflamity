package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

val contactAttacks = listOfNotNull(
    DamageCause.ENTITY_ATTACK,
    DamageCause.ENTITY_SWEEP_ATTACK,
    DamageCause.CRAMMING,
)

fun EntityDamageEvent.spreadsFire(): Boolean {
    if (this !is EntityDamageByEntityEvent) return false
    if (cause !in contactAttacks) return false

    val entityOnFire = entity.fireTicks > 0
    val damagerOnFire = damager.fireTicks > 0

    val oneOnFire = entityOnFire || damagerOnFire

    if (!oneOnFire) return false

    if (damager !is LivingEntity) return true

    return true
}

class EntityContactListener : Listener {
    @EventHandler fun onHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        if (!event.spreadsFire()) return

        entity.fireTicks = FIRE_DURATION
        damager.fireTicks = FIRE_DURATION
    }
}
