package dev.enderman.minecraft.plugins.fire.better.fire

import dev.enderman.minecraft.plugins.fire.better.utility.block.getNeighbours
import org.bukkit.block.Block

fun Block.supportsFire(): Boolean {
    val neighbours = getNeighbours()

    val oneNeighbourFlammable = neighbours.any { it.canBurn() }
    val canBeReplacedWithFire = !type.isCollidable && canBurn()

    return oneNeighbourFlammable && canBeReplacedWithFire
}

fun Block.canBurn(): Boolean {
    return type.isFlammable || type.isFuel || type.isBurnable
}
