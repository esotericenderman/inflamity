package dev.enderman.minecraft.plugins.fire.better

import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityBurnListener
import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityFireSpreadListener
import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityIgniteListener
import dev.enderman.minecraft.plugins.fire.better.events.listeners.FireExtinguishListener
import org.bukkit.plugin.java.JavaPlugin

open class InflamityPlugin : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(EntityIgniteListener(), this)
        server.pluginManager.registerEvents(FireExtinguishListener(), this)
        server.pluginManager.registerEvents(EntityFireSpreadListener(), this)
        server.pluginManager.registerEvents(EntityBurnListener(), this)
    }
}
