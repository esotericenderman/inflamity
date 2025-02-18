package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class EntityIgniteListener : Listener {

    @EventHandler
    fun onEntityIgnite(event: PlayerInteractAtEntityEvent) {
        val player = event.player
        val heldItem = player.inventory.itemInMainHand
        val otherItem = player.inventory.itemInOffHand

        val atLeastOneEmpty = heldItem.isEmpty || otherItem.isEmpty

        if (!atLeastOneEmpty) return

        if (heldItem.type != Material.FLINT_AND_STEEL && otherItem.type != Material.FLINT_AND_STEEL) return

        val entity = event.rightClicked

        entity.world.playSound(entity.location, Sound.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F)
        player.swingMainHand()
        entity.fireTicks = 40
    }
}
