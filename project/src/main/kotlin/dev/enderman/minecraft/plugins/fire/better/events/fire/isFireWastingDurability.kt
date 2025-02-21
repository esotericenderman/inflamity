package dev.enderman.minecraft.plugins.fire.better.events.fire

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

val durabilityWastingFireDamage = listOfNotNull(EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.CAMPFIRE)

fun EntityDamageEvent.isFireWastingDurability(): Boolean {
    return entity is LivingEntity && durabilityWastingFireDamage.contains(cause)
}
