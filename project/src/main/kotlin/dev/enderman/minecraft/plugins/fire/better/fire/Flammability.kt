package dev.enderman.minecraft.plugins.fire.better.fire

import dev.enderman.minecraft.plugins.fire.better.utility.block.getNeighbours
import dev.enderman.minecraft.plugins.fire.better.utility.box.loopBoundingBox
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity

/**
 * These materials burn infinitely, but do not burn away. Fire stays on them indefinitely, and they cannot be destroyed by it.
 */
val infiniteBurnBlocks = listOfNotNull(
    Material.NETHERRACK,
    Material.WARPED_NYLIUM,
    Material.CRIMSON_NYLIUM
)

/**
 * These materials represent liquid blocks.
 */
val liquidBlocks = listOfNotNull(
    Material.LAVA,
    Material.WATER
)

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

fun Entity.attemptFireSpread() {
    loopBoundingBox(Block::attemptFireSpread)
}

fun Block.attemptFireSpread() {
    if (supportsFire()) {
        type = Material.FIRE
    }
}
