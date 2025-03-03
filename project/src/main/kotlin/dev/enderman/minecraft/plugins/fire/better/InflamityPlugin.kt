package dev.enderman.minecraft.plugins.fire.better

import dev.enderman.minecraft.plugins.fire.better.events.lava.listeners.LavaDamageListener
import dev.enderman.minecraft.plugins.fire.better.events.listeners.*
import org.bukkit.GameMode
import org.bukkit.plugin.java.JavaPlugin

const val FIRE_DURATION = 10_000

val gameModesWithConsequences = listOfNotNull(
    GameMode.SURVIVAL,
    GameMode.ADVENTURE
)

open class InflamityPlugin : JavaPlugin() {

    override fun onEnable() {
        val manager = server.pluginManager

        val events = listOf(
            EntityIgniteListener(),
            FireExtinguishListener(),
            EntityFireSpreadListener(),
            EntityContactListener(),
            BurningSlimeSplitListener(),
            FireProjectileListener(),
            EntityCombustListener(),
            SuffocationListener(),
            FirePlaceListener(),
            LavaDamageListener(),
            ThrowableFireballListener(),
            FlammableBlockPlaceListener(),
            FlammableItemDestroyListener(),

            EntityBurnListener(this)
        )

        events.forEach { manager.registerEvents(it, this) }
    }
}
