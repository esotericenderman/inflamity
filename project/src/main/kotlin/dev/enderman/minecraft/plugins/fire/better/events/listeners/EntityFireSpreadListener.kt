package dev.enderman.minecraft.plugins.fire.better.events.listeners

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.damage.DamageType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class EntityFireSpreadListener : Listener {
    @EventHandler
    fun onEntityBurn(event: EntityDamageEvent) {
        val isFromFire = event.damageSource.damageType == DamageType.ON_FIRE

        if (!isFromFire) return

        val block = event.entity.location.block
        val blockBeneath = block.getRelative(BlockFace.DOWN)

        if (block.type == Material.AIR && blockBeneath.type != Material.AIR) block.type = Material.FIRE
    }
}
