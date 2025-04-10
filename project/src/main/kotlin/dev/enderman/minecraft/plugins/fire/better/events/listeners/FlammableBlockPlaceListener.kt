package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.fire.attemptFireSpread
import dev.enderman.minecraft.plugins.fire.better.gameModesWithConsequences
import dev.enderman.minecraft.plugins.fire.better.blocks.directions
import dev.enderman.minecraft.plugins.fire.better.entity.fire.combust
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class FlammableBlockPlaceListener : Listener {

    @EventHandler
    private fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        if (gameModesWithConsequences.contains(player.gameMode)) return

        val block = event.block

        val type = block.type

        if (!type.isAir && !type.isFlammable && !type.isFuel && !type.isBurnable) return

        if (event.blockReplacedState.type != Material.FIRE && event.blockReplacedState.type != Material.LAVA) return

        player.combust()

        directions.forEach {
            val blockInDirection = block.getRelative(it)

            blockInDirection.attemptFireSpread()
        }
    }
}