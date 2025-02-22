package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionFactor
import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionLevel
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.fire.isDurabilityWastingFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.suffocation.isSuffocationDamage
import dev.enderman.minecraft.plugins.fire.better.utility.loopArmor
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

        println("Entity ${entity.name} took damage from fire because of ${event.cause}.")

        entity.fireTicks = 10_000

        val container = entity.persistentDataContainer

        if (container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            println("Ignoring event as the damage was forced and artificial.")

            container.remove(plugin.ignoreFireKey)

            if (event.isDurabilityWastingFireDamage()) {
                entity.loopArmor {
                    if (it.itemMeta !is Damageable) return@loopArmor

                    val factor = it.getEnchantmentLevel(Enchantment.FIRE_PROTECTION) / Enchantment.FIRE_PROTECTION.maxLevel.toDouble()

                    if (Random.nextDouble() < factor) it.editMeta(Damageable::class.java) { meta ->
                        val itemContainer = meta.persistentDataContainer
                        val previousDamage = itemContainer[plugin.previousDamageKey, PersistentDataType.INTEGER]!!
                        itemContainer.remove(plugin.previousDamageKey)

                        meta.damage = previousDamage
                    }
                }
            }

            return
        }

        if (entity !is LivingEntity) return

        val factor = entity.getFireProtectionFactor()

        if (factor == 1.0) {
            event.isCancelled = true
            entity.fireTicks = 0
            return
        }

        if (factor != 0.0) {
            val final = event.finalDamage
            val toDeal = final * (1 - factor)

            event.isCancelled = true

            container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] = true

            entity.loopArmor {
                val meta = it.itemMeta;
                if (meta is Damageable) {
                    it.editMeta(Damageable::class.java) { editable ->
                        editable.persistentDataContainer[plugin.previousDamageKey, PersistentDataType.INTEGER] = editable.damage
                    }
                }
            }

            entity.damage(toDeal, event.damageSource)
        }
    }
}
