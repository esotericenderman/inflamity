package dev.enderman.minecraft.plugins.fire.better

import dev.enderman.minecraft.plugins.fire.better.events.listeners.*
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

open class InflamityPlugin : JavaPlugin() {

    val ignoreFireKey = NamespacedKey(this, "ignore_fire")
    val previousDamageKey = NamespacedKey(this, "previous_durability")

    override fun onEnable() {
        server.pluginManager.registerEvents(EntityIgniteListener(), this)
        server.pluginManager.registerEvents(FireExtinguishListener(), this)
        server.pluginManager.registerEvents(EntityFireSpreadListener(), this)
        server.pluginManager.registerEvents(EntityBurnListener(this), this)
        server.pluginManager.registerEvents(EntityContactListener(), this)
    }
}
