package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionFactor
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.fire.isDurabilityWastingFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.suffocation.isSuffocationDamage
import dev.enderman.minecraft.plugins.fire.better.utility.loopDamageableArmor
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
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

            if (!event.isDurabilityWastingFireDamage()) return

            entity.loopDamageableArmor { meta, item ->
                val factor = item.getFireProtectionFactor()

                if (Random.nextDouble() > factor) return@loopDamageableArmor

                val itemContainer = meta.persistentDataContainer
                val previousDamage = itemContainer[plugin.previousDamageKey, PersistentDataType.INTEGER]!!
                itemContainer.remove(plugin.previousDamageKey)

                meta.damage = previousDamage
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

        if (factor == 0.0) return

        val final = event.finalDamage
        val toDeal = final * (1 - factor)

        event.isCancelled = true

        container[plugin.ignoreFireKey, PersistentDataType.BOOLEAN] = true

        entity.loopDamageableArmor { meta, _ ->
            meta.persistentDataContainer[plugin.previousDamageKey, PersistentDataType.INTEGER] = meta.damage
        }

        entity.damage(toDeal, event.damageSource)
    }
}
