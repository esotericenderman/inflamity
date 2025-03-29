package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.entity.contact.spreadsFire
import dev.enderman.minecraft.plugins.fire.better.entity.fire.combust
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EntityContactListener : Listener {
    @EventHandler fun onHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        if (!event.spreadsFire()) return

        entity.combust()
        damager.combust()
    }
}
