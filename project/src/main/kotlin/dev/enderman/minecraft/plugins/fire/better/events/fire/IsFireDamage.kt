package dev.enderman.minecraft.plugins.fire.better.events.fire

import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

val fireDamageTypes = listOf(
    DamageCause.FIRE,
    DamageCause.FIRE_TICK,
    DamageCause.CAMPFIRE,
//    DamageType.HOT_FLOOR
)

fun EntityDamageEvent.isFireDamage(): Boolean {
    return cause in fireDamageTypes
}
