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

        val container = entity.persistentDataContainer

        if (container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            container.remove(plugin.ignoreFireKey)

            if (event.isDurabilityWastingFireDamage()) {
                (entity as LivingEntity).equipment?.armorContents?.forEach {
                    if (it?.itemMeta !is Damageable) return

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

        entity.fireTicks = 10_000
    }
}
