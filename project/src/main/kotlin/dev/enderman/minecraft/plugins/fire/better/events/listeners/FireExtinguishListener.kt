package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class FireExtinguishListener : Listener {

    @EventHandler
    fun onFireExtinguish(event: BlockBreakEvent) {
        val block = event.block
        if (block.type != Material.FIRE) return

        event.isCancelled = true
        event.player.fireTicks = 20
    }
}