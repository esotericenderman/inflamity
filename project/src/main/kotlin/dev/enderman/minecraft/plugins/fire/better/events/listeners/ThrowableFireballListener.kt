package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.gameModesWithConsequences
import org.bukkit.Material
import org.bukkit.entity.SmallFireball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class ThrowableFireballListener : Listener {

    @EventHandler
    private fun onFireballThrow(event: PlayerInteractEvent) {
        val block = event.clickedBlock
        if (block != null) return

        if (!event.action.isRightClick) return

        val heldItem = event.item
        val type = heldItem?.type

        if (type != Material.FIRE_CHARGE) return

        val hand = event.hand
        if (hand != EquipmentSlot.HAND) return

        val player = event.player
        player.launchProjectile(SmallFireball::class.java)

        player.swingHand(EquipmentSlot.HAND)

        if (!gameModesWithConsequences.contains(player.gameMode)) return

        heldItem.amount--
    }
}
