package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import dev.enderman.minecraft.plugins.fire.better.entity.isOnFire
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.InventoryHolder

val contactAttacks = listOfNotNull(
    DamageCause.ENTITY_ATTACK,
    DamageCause.ENTITY_SWEEP_ATTACK,
    DamageCause.CRAMMING,
)

fun EntityDamageEvent.spreadsFire(): Boolean {
    if (this !is EntityDamageByEntityEvent) return false
    if (cause !in contactAttacks) return false

    val entityOnFire = entity.isOnFire()
    val damagerOnFire = damager.isOnFire()

    val oneOnFire = entityOnFire || damagerOnFire

    if (!oneOnFire) return false

    if (damager !is LivingEntity) return true

    val cast = damager as LivingEntity

    var leftHanded = false
    if (cast is Mob) {
        leftHanded = cast.isLeftHanded
    }

    val equipment = cast.equipment

    val holding = if (leftHanded) equipment?.itemInOffHand else equipment?.itemInMainHand
    val material = holding?.type

    val flammable = material?.isFlammable == true
    val burnable = material?.isBurnable == true
    val fuel = material?.isFuel == true
    val air = material?.isAir == true

    return flammable || burnable || fuel || air || material == null
}

class EntityContactListener : Listener {
    @EventHandler fun onHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        if (!event.spreadsFire()) return

        entity.fireTicks = FIRE_DURATION
        damager.fireTicks = FIRE_DURATION
    }
}
