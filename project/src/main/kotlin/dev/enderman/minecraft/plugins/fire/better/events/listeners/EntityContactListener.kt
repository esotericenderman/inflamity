package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.damage.DamageType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EntityContactListener : Listener {
    @EventHandler fun onHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        val isDirect =
                   event.damageSource.damageType == DamageType.MOB_ATTACK
                || event.damageSource.damageType == DamageType.MOB_ATTACK_NO_AGGRO
                || event.damageSource.damageType == DamageType.PLAYER_ATTACK

        if (!isDirect) return

        val entityOnFire = entity.fireTicks > 0
        val damagerOnFire = damager.fireTicks > 0

        val oneOnFire = entityOnFire || damagerOnFire

        if (!oneOnFire) return

        entity.fireTicks = 10_000
        damager.fireTicks = 10_000
    }
}
