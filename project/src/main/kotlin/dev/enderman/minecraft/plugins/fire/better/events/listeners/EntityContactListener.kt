package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

val contactAttacks = listOfNotNull(
    DamageCause.ENTITY_ATTACK,
    DamageCause.ENTITY_SWEEP_ATTACK,
    DamageCause.CRAMMING,
)

class EntityContactListener : Listener {
    @EventHandler fun onHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        if (event.cause !in contactAttacks) return

        val entityOnFire = entity.fireTicks > 0
        val damagerOnFire = damager.fireTicks > 0

        val oneOnFire = entityOnFire || damagerOnFire

        if (!oneOnFire) return

        entity.fireTicks = 10_000
        damager.fireTicks = 10_000
    }
}
