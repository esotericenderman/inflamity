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

val liquidBlocks = listOfNotNull(
    Material.LAVA,
    Material.WATER
)

fun Block.supportsFire(): Boolean {
    val canBeReplaced = !type.isCollidable && !liquidBlocks.contains(type)
    if (!canBeReplaced) return false

    val neighbours = getNeighbours()

    val canBurn = canBurn()
    val oneNeighbourFlammable = neighbours.any { it.canBurn() }

    return canBurn || oneNeighbourFlammable
}

fun Block.canBurn(): Boolean {
    return type.canBurn()
}

/**
 * Returns whether this block should be able to disappear if it is set on fire.
 */
fun Material.canBurnAway(): Boolean {
    return isFlammable || isFuel || isBurnable
}

/**
 * Returns whether this material can support fire.
 */
fun Material.canBurn(): Boolean {
    return canBurnAway() || infiniteBurnBlocks.contains(this)
}

fun Entity.attemptFireSpread() {
    loopBoundingBox(Block::attemptFireSpread)
}

fun Block.attemptFireSpread() {
    if (supportsFire()) {
        type = Material.FIRE
    }
}
