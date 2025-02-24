package dev.enderman.minecraft.plugins.fire.better.utility.entity.passenger

import org.bukkit.entity.Entity

fun Entity.loopPassengers(action: (Entity) -> Unit) {
    passengers.forEach { action(it); it.loopPassengers(action) }
}

fun Entity.loopVehicle(action: (Entity) -> Unit) {
    vehicle?.loopVehicle { action(it); it.loopVehicle(action) }
}
