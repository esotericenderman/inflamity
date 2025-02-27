package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class FlammableBlockPlaceListener : Listener {

    @EventHandler
    private fun onBlockPlace(event: BlockPlaceEvent) {
        val block = event.block

        val type = block.type

        if (!type.isAir && !type.isFlammable && !type.isFuel && !type.isBurnable) return

        event.player.fireTicks = FIRE_DURATION
    }
}