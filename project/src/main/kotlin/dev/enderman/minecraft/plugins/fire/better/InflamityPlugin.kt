package dev.enderman.minecraft.plugins.fire.better

import dev.enderman.minecraft.plugins.fire.better.events.fireball.listeners.OtherProjectileListener
import dev.enderman.minecraft.plugins.fire.better.events.fireball.listeners.ThrowableFireballListener
import dev.enderman.minecraft.plugins.fire.better.events.lava.listeners.LavaDamageListener
import dev.enderman.minecraft.plugins.fire.better.events.listeners.*
import dev.enderman.minecraft.plugins.fire.better.events.suffocation.listeners.SuffocationListener
import org.bstats.bukkit.Metrics
import org.bukkit.GameMode
import org.bukkit.plugin.java.JavaPlugin

const val FIRE_DURATION = 10_000

val gameModesWithConsequences = listOfNotNull(
    GameMode.SURVIVAL,
    GameMode.ADVENTURE
)

open class InflamityPlugin : JavaPlugin() {

    companion object {
        const val METRICS_PLUGIN_ID = 25135
    }

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
            LavaDamageListener(),
            ThrowableFireballListener(),
            OtherProjectileListener(),
            FlammableBlockPlaceListener(),
            FlammableItemDestroyListener(),

            FirePlaceListener(this),
            EntityBurnListener(this)
        )

        events.forEach { manager.registerEvents(it, this) }

        try {
            Metrics(this, METRICS_PLUGIN_ID)
        } catch (exception: IllegalStateException) {
            logger.warning("Failed to load metrics, likely due to non-production environment.")
            exception.printStackTrace()
        }
    }
}
