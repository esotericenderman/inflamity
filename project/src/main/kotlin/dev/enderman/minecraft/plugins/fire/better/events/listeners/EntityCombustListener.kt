package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent

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
}
