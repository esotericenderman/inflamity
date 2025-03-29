package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionFactor
import dev.enderman.minecraft.plugins.fire.better.entity.fire.extinguish
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import dev.enderman.minecraft.plugins.fire.better.armor.loopDamageableArmor
import dev.enderman.minecraft.plugins.fire.better.armor.loopDamageableArmorMeta
import dev.enderman.minecraft.plugins.fire.better.entity.fire.combust
import org.bukkit.NamespacedKey
import org.bukkit.entity.Creeper
import org.bukkit.entity.Entity

import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

/**
 * A list of entities that are COMPLETELY IMMUNE to fire.
 *
 * These entities will not catch fire, and they will not take damage from fire.
 */
val fireImmuneEntities = listOfNotNull(
    EntityType.IRON_GOLEM,
    EntityType.ENDER_PEARL,
    EntityType.EGG,
    EntityType.POTION,
    EntityType.SKELETON
)

fun Entity.isImmuneToFire(): Boolean {
    return fireImmuneEntities.contains(type) || (this is Creeper && this.isPowered)
}

class EntityBurnListener(private val plugin: JavaPlugin) : Listener {

    private val ignoreFireKey = NamespacedKey(plugin, "ignore_fire")
    private val previousDamageKey = NamespacedKey(plugin, "previous_durability")

    @EventHandler(priority = EventPriority.NORMAL)
    fun onEntityBurn(event: EntityDamageEvent) {
        if (event.isCancelled) return
        if (!event.isFireDamage()) return

        val entity = event.entity
        if (entity is Creeper && !entity.isPowered) entity.ignite()

        val container = entity.persistentDataContainer

        if (container[ignoreFireKey, PersistentDataType.BOOLEAN] == true) {
            container.remove(ignoreFireKey)
            return
        }

        entity.combust()

        entity.passengers.forEach { it.combust() }
        entity.vehicle?.combust()

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
