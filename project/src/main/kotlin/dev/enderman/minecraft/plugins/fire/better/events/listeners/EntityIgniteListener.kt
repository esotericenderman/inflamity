package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.apache.commons.lang3.BooleanUtils
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

        val mainHandHolding = heldItem.type == Material.FLINT_AND_STEEL
        val offHandHolding = otherItem.type == Material.FLINT_AND_STEEL

        val oneHandHolding = BooleanUtils.xor(mainHandHolding, offHandHolding)
        if (!oneHandHolding) return

        val entity = event.rightClicked

        entity.world.playSound(entity.location, Sound.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F)

        if (mainHandHolding) player.swingMainHand() else player.swingOffHand()

        entity.fireTicks = 10_000
    }
}
