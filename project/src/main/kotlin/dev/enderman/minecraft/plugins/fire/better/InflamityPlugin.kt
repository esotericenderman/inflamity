package dev.enderman.minecraft.plugins.fire.better

import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityIgniteListener
import org.bukkit.plugin.java.JavaPlugin

open class InflamityPlugin : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(EntityIgniteListener(), this)
    }
}
