package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.entity.extinguish
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
}
