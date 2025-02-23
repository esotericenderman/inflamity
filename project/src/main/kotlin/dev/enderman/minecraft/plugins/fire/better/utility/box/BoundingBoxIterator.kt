package dev.enderman.minecraft.plugins.fire.better.utility.box

import org.bukkit.block.Block
import org.bukkit.entity.Entity

fun Entity.loopBoundingBox(action: (Block) -> Unit) {
    val minX = boundingBox.minX.toInt()
    val minY = boundingBox.minY.toInt()
    val minZ = boundingBox.minZ.toInt()
    val maxX = boundingBox.maxX.toInt()
    val maxY = boundingBox.maxY.toInt()
    val maxZ = boundingBox.maxZ.toInt()

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                val block = world.getBlockAt(x, y, z)

                action(block)
            }
        }
    }
}
