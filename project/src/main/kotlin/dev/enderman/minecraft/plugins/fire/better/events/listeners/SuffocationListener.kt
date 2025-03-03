package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.entity.fire.extinguish
import dev.enderman.minecraft.plugins.fire.better.events.suffocation.isSuffocationDamage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class SuffocationListener : Listener {
    @EventHandler
    private fun onSuffocate(event: EntityDamageEvent) {
        if (!event.isSuffocationDamage()) return

        event.entity.extinguish()
    }
}
