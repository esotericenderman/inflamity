package dev.enderman.minecraft.plugins.fire.better.fire

import dev.enderman.minecraft.plugins.fire.better.utility.block.getNeighbours
import dev.enderman.minecraft.plugins.fire.better.utility.box.loopBoundingBox
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity

val infiniteBurnBlocks = listOfNotNull(
    Material.NETHERRACK,
    Material.WARPED_NYLIUM,
    Material.CRIMSON_NYLIUM
)

fun Block.supportsFire(): Boolean {
    val neighbours = getNeighbours()

    val canBeReplaced = !type.isCollidable

    if (!canBeReplaced) return false

    val canBurn = canBurn()
    val oneNeighbourFlammable = neighbours.any { it.canBurn() }

    return canBurn || oneNeighbourFlammable
}

fun Block.canBurn(): Boolean {
    return type.isFlammable || type.isFuel || type.isBurnable || infiniteBurnBlocks.contains(type)
}

fun Entity.attemptFireSpread() {
    loopBoundingBox(Block::attemptFireSpread)
}

fun Block.attemptFireSpread() {
    if (supportsFire()) {
        type = Material.FIRE
    }
}
