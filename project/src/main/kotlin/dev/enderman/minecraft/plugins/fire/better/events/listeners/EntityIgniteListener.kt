package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class EntityIgniteListener : Listener {

    @EventHandler
    private fun onEntityIgnite(event: PlayerInteractAtEntityEvent) {
        val player = event.player
        val heldItem = player.inventory.itemInMainHand

        if (heldItem.type != Material.FLINT_AND_STEEL) return

        val entity = event.rightClicked

        entity.world.playSound(entity.location, Sound.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F)
        entity.fireTicks = 40
    }
}
