package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionFactor
import dev.enderman.minecraft.plugins.fire.better.entity.extinguish
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.suffocation.isSuffocationDamage
import dev.enderman.minecraft.plugins.fire.better.utility.armor.loopDamageableArmor
import dev.enderman.minecraft.plugins.fire.better.utility.armor.loopDamageableArmorMeta
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import kotlin.math.roundToInt
import kotlin.random.Random

class EntityBurnListener(private val plugin: InflamityPlugin) : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onEntityBurn(event: EntityDamageEvent) {
        val entity = event.entity

        if (event.isSuffocationDamage()) {
            entity.extinguish()
            return
        }

        if (!event.isFireDamage()) return

        val container = entity.persistentDataContainer

        if (container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            container.remove(plugin.ignoreFireKey)
            return
        }

        val originalFire = entity.fireTicks

        entity.fireTicks = 10_000

        if (entity !is LivingEntity) return

        val factor = entity.getFireProtectionFactor()

        if (factor == 1.0) {
            event.isCancelled = true
            entity.extinguish()
            return
        }

        if (factor == 0.0) return

        val final = event.finalDamage

        val inverseFactor = 1 - factor
        val toDeal = final * inverseFactor

        event.isCancelled = true

        container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] = true

        entity.loopDamageableArmorMeta { meta ->
            meta.persistentDataContainer[plugin.previousDamageKey, PersistentDataType.INTEGER] = meta.damage
        }

        plugin.server.scheduler.runTaskLater(
            plugin,
            { ->
                entity.loopDamageableArmor { meta, item ->
                    val itemFactor = item.getFireProtectionFactor()

                    if (Random.nextDouble() > itemFactor) return@loopDamageableArmor

                    val itemContainer = meta.persistentDataContainer
                    val previousDamage = itemContainer[plugin.previousDamageKey, PersistentDataType.INTEGER] ?: return@loopDamageableArmor

                    itemContainer.remove(plugin.previousDamageKey)

                    meta.damage = previousDamage
                }
            },
            1L
        )

        entity.fireTicks = (originalFire * inverseFactor).roundToInt()

        entity.damage(toDeal, event.damageSource)
    }
}
