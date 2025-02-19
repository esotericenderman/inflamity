package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.damage.DamageType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

val contactAttacks = listOfNotNull(DamageType.PLAYER_ATTACK, DamageType.MOB_ATTACK, DamageType.MOB_ATTACK_NO_AGGRO)

class EntityContactListener : Listener {
    @EventHandler fun onHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        val isDirect = event.damageSource.damageType in contactAttacks

        if (!isDirect) return

        val entityOnFire = entity.fireTicks > 0
        val damagerOnFire = damager.fireTicks > 0

        val oneOnFire = entityOnFire || damagerOnFire

        if (!oneOnFire) return

        entity.fireTicks = 10_000
        damager.fireTicks = 10_000
    }
}
