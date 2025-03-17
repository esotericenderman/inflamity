package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.fire.supportsFire
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.block.BlockPlaceEvent

class FirePlaceListener : Listener {
    @EventHandler
    private fun onFirePlace(event: BlockPlaceEvent) {
        val block = event.block
        if (block.type != Material.FIRE) return

        if (!block.supportsFire()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onFirePlace(event: BlockIgniteEvent) {
        val block = event.block

        if (!block.supportsFire()) {
            event.isCancelled = true
        }
    }
}
