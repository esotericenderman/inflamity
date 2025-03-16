package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.fire.attemptFireSpread
import dev.enderman.minecraft.plugins.fire.better.fire.supportsFire
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class EntityFireSpreadListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityBurn(event: EntityDamageEvent) {
        if (!event.isFireDamage()) return
        if (event.isCancelled) return

        event.entity.attemptFireSpread()
    }
}
