package dev.enderman.minecraft.plugins.fire.better.entity.contact

import dev.enderman.minecraft.plugins.fire.better.entity.isOnFire
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

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
