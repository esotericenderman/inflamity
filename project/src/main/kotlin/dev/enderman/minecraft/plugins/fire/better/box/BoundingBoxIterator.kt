package dev.enderman.minecraft.plugins.fire.better.box

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity

fun Entity.loopBoundingBox(action: (Block) -> Unit) {
    val box = boundingBox

    var currentX = box.minX
    var currentY = box.minY
    var currentZ = box.minZ

    while (currentX <= box.maxX) {
        while (currentZ <= box.maxZ) {
            while (currentY <= box.minY) {
                val block = world.getBlockAt(Location(world, currentX, currentY, currentZ))

                action(block)

                currentY++
            }

            currentZ++
        }

        currentX++
    }
}
