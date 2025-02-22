package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import dev.enderman.minecraft.plugins.fire.better.entity.isOnFire
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

class FireProjectileShootListener : Listener {
    @EventHandler
    private fun onProjectileShoot(event: ProjectileLaunchEvent) {
        val projectile = event.entity
        val shooter = projectile.shooter

        if (shooter !is Entity) return
        if (!shooter.isOnFire()) return

        projectile.fireTicks = FIRE_DURATION
    }
}
