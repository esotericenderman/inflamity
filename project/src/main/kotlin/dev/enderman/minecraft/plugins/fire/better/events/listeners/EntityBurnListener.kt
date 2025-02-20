package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType

class EntityBurnListener(private val plugin: InflamityPlugin) : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onEntityBurn(event: EntityDamageEvent) {
        val entity = event.entity

        val isSuffocating = event.cause == EntityDamageEvent.DamageCause.SUFFOCATION
        if (isSuffocating) {
            entity.fireTicks = 0
            return
        }

        if (!event.isFireDamage()) return

        val container = entity.persistentDataContainer

        if (container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            container.remove(plugin.ignoreFireKey)
            return
        }

        if (entity is LivingEntity) {
            val equipment = entity.equipment

            val head = equipment?.helmet
            val chest = equipment?.chestplate
            val legs = equipment?.leggings
            val feet = equipment?.boots

            val headLevel = head?.itemMeta?.getEnchantLevel(Enchantment.FIRE_PROTECTION) ?: 0
            val chestLevel = chest?.itemMeta?.getEnchantLevel(Enchantment.FIRE_PROTECTION) ?: 0
            val legsLevel = legs?.itemMeta?.getEnchantLevel(Enchantment.FIRE_PROTECTION) ?: 0
            val feetLevel = feet?.itemMeta?.getEnchantLevel(Enchantment.FIRE_PROTECTION) ?: 0

            val total = headLevel + chestLevel + legsLevel + feetLevel

            if (total == 16) {
                event.isCancelled = true
                entity.fireTicks = 0
                return
            }

            val damageFactor = (16.0 - total) / 16.0
            val final = event.finalDamage

            val toDeal = final * damageFactor
            event.isCancelled = true

            container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] = true

            entity.damage(toDeal, event.damageSource)
            return
        }

        entity.fireTicks = 10_000
    }
}
