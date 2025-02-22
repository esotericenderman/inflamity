package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import dev.enderman.minecraft.plugins.fire.better.entity.isOnFire
import org.bukkit.Material
import org.bukkit.block.data.type.Candle
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent

class FireProjectileListener : Listener {
    @EventHandler
    private fun onProjectileShoot(event: ProjectileLaunchEvent) {
        val projectile = event.entity
        val shooter = projectile.shooter

        if (shooter !is Entity) return
        if (!shooter.isOnFire()) return

        projectile.fireTicks = FIRE_DURATION
    }

    @EventHandler
    private fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        if (!projectile.isOnFire()) return

        val hitBlock = event.hitBlock ?: return

        val data = hitBlock.blockData

        val type = hitBlock.type
        val name = type.name

        if (name.endsWith("_CANDLE")) {
            (data as Candle).isLit = true
        }

        hitBlock.blockData = data

        val projectileBlock = projectile.location.block
        if (projectileBlock.type != Material.AIR) return

        if (hitBlock.type.isFlammable || hitBlock.type.isFuel) projectileBlock.type = Material.FIRE
    }
}
