package dev.enderman.minecraft.plugins.fire.better.events.fire

import org.bukkit.damage.DamageType
import org.bukkit.event.entity.EntityDamageEvent

val fireDamageTypes = listOf(
    DamageType.IN_FIRE,
    DamageType.ON_FIRE,
    DamageType.CAMPFIRE,
//    DamageType.HOT_FLOOR
)

fun EntityDamageEvent.isFireDamage(): Boolean {
    return damageSource.damageType in fireDamageTypes
}
