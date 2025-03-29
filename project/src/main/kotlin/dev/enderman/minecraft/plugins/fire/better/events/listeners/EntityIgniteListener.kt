package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.entity.fire.combust
import dev.enderman.minecraft.plugins.fire.better.gameModesWithConsequences
import org.apache.commons.lang3.BooleanUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.meta.Damageable

/**
 * Entities that can't be ignited with a flint and steel, usually because they have some sort of other behaviour when right-clicking them with a flint and steel.
 */
val nonIgnitableEntities = listOfNotNull(
    EntityType.CREEPER
)

class EntityIgniteListener : Listener {

    @EventHandler
    fun onEntityIgnite(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked

        if (nonIgnitableEntities.contains(entity.type)) return

        if (event.hand != EquipmentSlot.HAND) return

        val player = event.player
        val heldItem = player.inventory.itemInMainHand
        val otherItem = player.inventory.itemInOffHand

        val atLeastOneEmpty = heldItem.isEmpty || otherItem.isEmpty

        if (!atLeastOneEmpty) return

        val mainHandHolding = heldItem.type == Material.FLINT_AND_STEEL
        val offHandHolding = otherItem.type == Material.FLINT_AND_STEEL

        val oneHandHolding = BooleanUtils.xor(mainHandHolding, offHandHolding)
        if (!oneHandHolding) return

        entity.world.playSound(entity.location, Sound.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F)

        if (mainHandHolding) player.swingMainHand() else player.swingOffHand()

        entity.combust()

        if (!gameModesWithConsequences.contains(player.gameMode)) return

        // Consequences:
        val item = if (mainHandHolding) heldItem else otherItem

        val meta = item.itemMeta as Damageable
        val damage = meta.damage
        val maxDamage = Material.FLINT_AND_STEEL.maxDurability

        if (damage == maxDamage - 1) {
            entity.world.playSound(entity.location, Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F)
        }

        item.damage(1, player)

        if (entity is Mob && gameModesWithConsequences.contains(player.gameMode)) entity.target = player
    }
}
