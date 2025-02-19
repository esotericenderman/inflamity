package dev.enderman.minecraft.plugins.fire.better.fire

import org.bukkit.damage.DamageType
import org.bukkit.event.entity.EntityDamageEvent

val fireDamageTypes = listOf(
    DamageType.IN_FIRE,
    DamageType.ON_FIRE,
    DamageType.CAMPFIRE,
//    DamageType.HOT_FLOOR
)

fun isFireDamage(event: EntityDamageEvent): Boolean {
    val damageType = event.damageSource.damageType

    return damageType in fireDamageTypes
}
