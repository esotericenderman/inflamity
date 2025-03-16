package dev.enderman.minecraft.plugins.fire.better.utility.block

import org.bukkit.block.Block
import org.bukkit.block.BlockFace

val directions = listOfNotNull(
    BlockFace.NORTH,
    BlockFace.EAST,
    BlockFace.SOUTH,
    BlockFace.WEST,
    BlockFace.UP,
    BlockFace.DOWN,
)

fun Block.getNeighbours(): List<Block> {
    val neighbours = mutableListOf<Block>()

    for (face in directions) {
        neighbours.add(getRelative(face))
    }

    return neighbours
}
