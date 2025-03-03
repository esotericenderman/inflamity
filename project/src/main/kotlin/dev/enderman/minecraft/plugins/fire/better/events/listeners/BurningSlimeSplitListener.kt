package dev.enderman.minecraft.plugins.fire.better.events.listeners

import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import dev.enderman.minecraft.plugins.fire.better.entity.fire.isOnFire
import org.bukkit.entity.Slime
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent

class BurningSlimeSplitListener : Listener {

    @EventHandler
    private fun onEntityTransform(event: EntityTransformEvent) {
        if (event.transformReason != EntityTransformEvent.TransformReason.SPLIT) return

        val old = event.entity
        if (!old.isOnFire() || old !is Slime) return

        event.transformedEntities.forEach { if (it is Slime) it.fireTicks = FIRE_DURATION }
    }
}
