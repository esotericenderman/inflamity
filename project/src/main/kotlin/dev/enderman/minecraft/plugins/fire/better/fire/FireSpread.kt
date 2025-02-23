package dev.enderman.minecraft.plugins.fire.better.fire

import dev.enderman.minecraft.plugins.fire.better.utility.block.getNeighbours
import dev.enderman.minecraft.plugins.fire.better.utility.box.loopBoundingBox
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity

fun Block.supportsFire(): Boolean {
    val neighbours = getNeighbours()

    val oneNeighbourFlammable = neighbours.any { it.canBurn() }
    val canBeReplacedWithFire = !type.isCollidable && canBurn()

    return oneNeighbourFlammable && canBeReplacedWithFire
}

fun Block.canBurn(): Boolean {
    return type.isFlammable || type.isFuel || type.isBurnable
}

fun Entity.attemptFireSpread() {
    loopBoundingBox {
        block -> if (block.supportsFire()) block.type = Material.FIRE
    }
}
