package dev.enderman.minecraft.plugins.fire.better

import dev.enderman.minecraft.plugins.fire.better.events.listeners.*
import org.bukkit.plugin.java.JavaPlugin

open class InflamityPlugin : JavaPlugin() {

    override fun onEnable() {
        val manager = server.pluginManager

        val events = listOf(
            EntityIgniteListener(),
            FireExtinguishListener(),
            EntityFireSpreadListener(),
            EntityContactListener(),
            BurningSlimeSplitListener(),

            EntityBurnListener(this)
        )

        events.forEach { manager.registerEvents(it, this) }
    }
}
