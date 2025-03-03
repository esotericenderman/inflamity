package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.fire.canBurnAway
import dev.enderman.minecraft.plugins.fire.better.gameModesWithConsequences
import dev.enderman.minecraft.plugins.fire.better.utility.armor.loopEquipmentItems
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class FlammableItemDestroyListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    private fun onFireDamage(event: EntityDamageEvent) {
        if (!event.isFireDamage()) return

        val entity = event.entity

        if (entity !is LivingEntity) return
        if (entity is Player && !gameModesWithConsequences.contains(entity.gameMode)) return

        entity.loopEquipmentItems {
            if (!it.type.canBurnAway()) return@loopEquipmentItems

            it.amount = 0
        }
    }
}