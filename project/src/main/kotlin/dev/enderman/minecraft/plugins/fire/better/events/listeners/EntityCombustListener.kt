package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.entity.fire.extinguish
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.event.entity.EntityDamageEvent

/**
 * The list of entities that instantly die (are removed) when they enter fire.
 */
val extremeCombustionEntities = listOfNotNull(
    EntityType.SNOWBALL
)

/**
 * This is a list of entities that have no means of sustaining a fire.
 *
 * This does not mean that they will not take damage while standing in fire.
 *
 * For example, a snow golem will still be damaged when standing in fire, but it makes no sense for it to burn as there isn't anything *to* burn and sustain the fire.
 *
 * In other words, these entities will not catch fire, but will still take damage while standing in fire blocks.
 */
val nonFlammableEntities = listOfNotNull(
    EntityType.SNOW_GOLEM
)

class EntityCombustListener : Listener {
    @EventHandler
    private fun onEntityCombust(event: EntityCombustEvent) {
        val entity = event.entity
        val type = entity.type

        if (!extremeCombustionEntities.contains(type)) return

        entity.remove()
    }

    @EventHandler
    private fun onFireImmuneCombust(event: EntityCombustEvent) {
        val entity = event.entity

        if (!entity.isImmuneToFire()) return

        entity.extinguish()
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOW)
    private fun onEntityBurn(event: EntityDamageEvent) {
        if (!event.isFireDamage()) return

        val entity = event.entity

        if (!entity.isImmuneToFire()) return

        entity.extinguish()
        event.isCancelled = true
    }

    @EventHandler
    private fun onNonFlammableBurn(event: EntityDamageEvent) {
        if (!event.isFireDamage()) return
        if (event.cause != EntityDamageEvent.DamageCause.FIRE_TICK) return

        val entity = event.entity

        if (!nonFlammableEntities.contains(entity.type)) return

        entity.extinguish()
        event.isCancelled = true
    }
}
