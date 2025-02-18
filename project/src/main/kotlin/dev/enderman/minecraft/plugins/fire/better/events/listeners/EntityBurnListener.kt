package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.damage.DamageType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class EntityBurnListener : Listener {
    @EventHandler fun onEntityBurn(event: EntityDamageEvent) {
        val source = event.damageSource

        val entity = event.entity

        val isSuffocating = source.damageType == DamageType.IN_WALL
        if (isSuffocating) {
            entity.fireTicks = 0
            return
        }

        val isFromFire = source.damageType == DamageType.ON_FIRE || source.damageType == DamageType.IN_FIRE || source.damageType == DamageType.CAMPFIRE

        if (!isFromFire) return

        entity.fireTicks = 10_000
    }
}
