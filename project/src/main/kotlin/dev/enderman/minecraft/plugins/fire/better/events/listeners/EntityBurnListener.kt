package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.fire.isDurabilityWastingFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.fire.isSuffocationDamage
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

class EntityBurnListener(private val plugin: InflamityPlugin) : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onEntityBurn(event: EntityDamageEvent) {
        val entity = event.entity
        if (event.isSuffocationDamage()) {
            entity.fireTicks = 0
            return
        }

        if (!event.isFireDamage()) return

        println("Entity ${entity.name} took fire damage.")

        val container = entity.persistentDataContainer

        if (entity is LivingEntity) {
            entity.equipment?.armorContents?.forEach {
                val meta = it.itemMeta; if (meta is Damageable) println("${it.type}: ${meta.damage}")
            }
        }

        if (container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            println("Ignoring this event.")
            container.remove(plugin.ignoreFireKey)

            if (event.isDurabilityWastingFireDamage()) {
                (entity as LivingEntity).equipment?.armorContents?.forEach {
                    if (it?.itemMeta !is Damageable) return

                    val factor = it.getEnchantmentLevel(Enchantment.FIRE_PROTECTION) / Enchantment.FIRE_PROTECTION.maxLevel.toDouble()

                    if (Random.nextDouble() < factor) it.editMeta(Damageable::class.java) { meta ->
                        if (meta.damage != 0) {
                            meta.damage--
                        }
                    }
                }
            }

            return
        }

        if (entity is LivingEntity) {
            var total = 0

            entity.equipment?.armorContents?.forEach { total += it.getEnchantmentLevel(Enchantment.FIRE_PROTECTION) }

            if (total == 16) {
                event.isCancelled = true
                entity.fireTicks = 0
                return
            }

            val factor = (16.0 - total) / 16.0

            if (total != 0) {
                val final = event.finalDamage
                val toDeal = final * factor

                event.isCancelled = true

                container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] = true

                entity.equipment?.armorContents?.forEach {
                    val meta = it.itemMeta; if (meta is Damageable) println("${it.type}: ${meta.damage}")
                }

                entity.damage(toDeal, event.damageSource)
            }
        }

        entity.fireTicks = 10_000
    }
}
