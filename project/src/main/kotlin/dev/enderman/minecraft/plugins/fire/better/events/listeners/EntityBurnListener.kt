package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class EntityBurnListener : Listener {
    @EventHandler fun onEntityBurn(event: EntityDamageEvent) {
        val entity = event.entity

        val isSuffocating = event.cause == EntityDamageEvent.DamageCause.SUFFOCATION
        if (isSuffocating) {
            entity.fireTicks = 0
            return
        }

        if (!event.isFireDamage()) return

        entity.fireTicks = 10_000
    }
}
