package dev.enderman.minecraft.plugins.fire.better.events.fireball.listeners

import org.bukkit.Material
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

class OtherProjectileListener : Listener {

    @EventHandler private fun onProjectileThrow(event: ProjectileLaunchEvent) {
        val projectile = event.entity

        if (projectile is Fireball) return

        val shooter = projectile.shooter

        if (shooter !is Player) return

        val mainHand = shooter.inventory.itemInMainHand
        val offHand = shooter.inventory.itemInOffHand

        if (mainHand.type == offHand.type) return
        if (mainHand.type == Material.FIRE_CHARGE) {
            event.isCancelled = true
        }
    }
}
