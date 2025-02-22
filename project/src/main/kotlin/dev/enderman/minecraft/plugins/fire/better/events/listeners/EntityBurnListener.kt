package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionFactor
import dev.enderman.minecraft.plugins.fire.better.entity.extinguish
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.events.suffocation.isSuffocationDamage
import dev.enderman.minecraft.plugins.fire.better.utility.armor.loopDamageableArmor
import dev.enderman.minecraft.plugins.fire.better.utility.armor.loopDamageableArmorMeta
import org.bukkit.NamespacedKey
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class EntityBurnListener(private val plugin: JavaPlugin) : Listener {

    private val ignoreFireKey = NamespacedKey(plugin, "ignore_fire")
    private val previousDamageKey = NamespacedKey(plugin, "previous_durability")

    @EventHandler(priority = EventPriority.LOW)
    fun onEntityBurn(event: EntityDamageEvent) {
        val entity = event.entity

        if (event.isSuffocationDamage()) {
            entity.extinguish()
            return
        }

        if (!event.isFireDamage()) return

        val container = entity.persistentDataContainer

        if (container[ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            container.remove(ignoreFireKey)
            return
        }

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
        val toDeal = final * (1 - factor)

        event.isCancelled = true

        container[ignoreFireKey, PersistentDataType.BOOLEAN] = true

        entity.loopDamageableArmorMeta { meta ->
            meta.persistentDataContainer[previousDamageKey, PersistentDataType.INTEGER] = meta.damage
        }

        plugin.server.scheduler.runTaskLater(
            plugin,
            { ->
                entity.loopDamageableArmor { meta, item ->
                    val itemFactor = item.getFireProtectionFactor()

                    if (Random.nextDouble() > itemFactor) return@loopDamageableArmor

                    val itemContainer = meta.persistentDataContainer
                    val previousDamage = itemContainer[previousDamageKey, PersistentDataType.INTEGER] ?: return@loopDamageableArmor

                    itemContainer.remove(previousDamageKey)

                    meta.damage = previousDamage
                }
            },
            1L
        )

        entity.damage(toDeal, event.damageSource)
    }
}
