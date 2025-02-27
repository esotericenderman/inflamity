package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import org.bukkit.entity.Fireball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

class FireChargeHitListener : Listener {

    @EventHandler
    private fun onFireChargeHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        if (projectile !is Fireball) return
        if (!projectile.isIncendiary) return

        val hitEntity = event.hitEntity ?: return
        hitEntity.fireTicks = FIRE_DURATION
    }
}