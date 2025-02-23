package dev.enderman.minecraft.plugins.fire.better.utility.block

import org.bukkit.block.Block
import org.bukkit.block.BlockFace

fun Block.getNeighbours(): List<Block> {
    val neighbours = mutableListOf<Block>()

    for (face in BlockFace.entries) {
        neighbours.add(getRelative(face))
    }

    return neighbours
}
