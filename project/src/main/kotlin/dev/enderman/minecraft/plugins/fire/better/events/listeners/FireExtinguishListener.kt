package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.entity.fire.ignite
import dev.enderman.minecraft.plugins.fire.better.gameModesWithConsequences
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class FireExtinguishListener : Listener {

    @EventHandler
    fun onFireExtinguish(event: BlockBreakEvent) {
        val block = event.block
        if (block.type != Material.FIRE) return

        val player = event.player

        if (!gameModesWithConsequences.contains(player.gameMode)) return

        event.isCancelled = true
        player.ignite()
    }
}