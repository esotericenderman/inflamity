package dev.enderman.minecraft.plugins.fire.better.fire

import dev.enderman.minecraft.plugins.fire.better.utility.block.getNeighbours
import org.bukkit.block.Block

fun Block.supportsFire(): Boolean {
    val neighbours = getNeighbours()

    val atLeastOneFlammable = neighbours.any { it.type.isFlammable || it.type.isFlammable || it.type.isBurnable }
    val isNotSolid = !type.isCollidable

    return atLeastOneFlammable && isNotSolid
}
