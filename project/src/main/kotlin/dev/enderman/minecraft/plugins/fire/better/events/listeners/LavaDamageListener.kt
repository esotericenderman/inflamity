package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getFireProtectionLevel
import dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection.getMaxFireProtectionLevel
import dev.enderman.minecraft.plugins.fire.better.events.lava.isLavaDamage
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

val lavaResistantEnemies = listOfNotNull(
    EntityType.IRON_GOLEM
)

fun Entity.calculateLavaDamageReduction(): Double {
    if (lavaResistantEnemies.contains(type)) {
        return 0.1
    }

    val fireProtection = getFireProtectionLevel()
    val max = getMaxFireProtectionLevel()

    if (fireProtection == 0 || max == 0) return 1.0

    val reducedFactor = fireProtection / (max * 3.0)
    val inverseFactor = 1.0 - reducedFactor

    return inverseFactor
}

class LavaDamageListener : Listener {

    @EventHandler
    private fun onLavaDamage(event: EntityDamageEvent) {
        if (!event.isLavaDamage()) return

        event.damage *= 5 * event.entity.calculateLavaDamageReduction()
    }
}
