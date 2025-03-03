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
 * Returns whether this material is flammable (can burn) *and* that fire destroys it.
 */
fun Material.canBurnAway(): Boolean {
    return isFlammable || isFuel || isBurnable
}

/**
 * Returns whether this material can be set on fire, but won't necessarily be destroyed by fire.
 * @see Material.canBurnAway
 */
fun Material.canBurn(): Boolean {
    return canBurnAway() || infiniteBurnBlocks.contains(this)
}

/**
 * Returns whether this material can burn infinitely, but does not get destroyed by the fire.
 */
fun Material.burnsInfinitely(): Boolean {
    return infiniteBurnBlocks.contains(this)
}

/**
 * Returns whether this block is flammable (can burn) *and* that fire destroys it.
 */
fun Block.canBurnAway(): Boolean {
    return type.canBurnAway()
}

/**
 * Returns whether this block can be set on fire, but won't necessarily be destroyed by fire.
 * @see Block.canBurnAway
 */
fun Block.canBurn(): Boolean {
    return type.canBurn()
}

/**
 * Returns whether this block can burn infinitely, but does not get destroyed by the fire.
 */
fun Block.burnsInfinitely(): Boolean {
    return type.burnsInfinitely()
}

fun Block.supportsFire(): Boolean {
    val canBeReplaced = !type.isCollidable && !liquidBlocks.contains(type)
    if (!canBeReplaced) return false

    val neighbours = getNeighbours()

    val canBurn = canBurn()
    val oneNeighbourFlammable = neighbours.any { it.canBurn() }

    return canBurn || oneNeighbourFlammable
}

fun Block.attemptFireSpread() {
    if (supportsFire()) {
        type = Material.FIRE
    }
}

fun Entity.attemptFireSpread() {
    loopBoundingBox(Block::attemptFireSpread)
}
