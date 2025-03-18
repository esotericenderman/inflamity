package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.fire.supportsFire
import dev.enderman.minecraft.plugins.fire.better.gameModesWithConsequences
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.plugin.Plugin

class FirePlaceListener(private val plugin : Plugin) : Listener {
    @EventHandler
    private fun onFirePlace(event: BlockIgniteEvent) {
        val block = event.block

        if (block.supportsFire()) return

        event.isCancelled = true

        val player = event.player ?: return

        val heldItem = player.inventory.itemInMainHand

        if (heldItem.type != Material.FLINT_AND_STEEL) return

        if (!gameModesWithConsequences.contains(player.gameMode)) return

        val meta = heldItem.itemMeta as Damageable
        val damage = meta.damage
        val maxDamage = Material.FLINT_AND_STEEL.maxDurability

        plugin.server.scheduler.runTaskLater(
            plugin,
            Runnable {
                if (damage == maxDamage - 1) {
                    player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F)
                }

                heldItem.damage(1, player)
            },
            1L
        )
    }
}
