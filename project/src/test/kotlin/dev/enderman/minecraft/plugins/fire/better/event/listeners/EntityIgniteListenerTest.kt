package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityIgniteListener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Sheep
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.entity.EntityMock
import org.mockbukkit.mockbukkit.entity.PlayerMock
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.*

class EntityIgniteListenerTest : AbstractInflamityPluginTest() {
    private lateinit var world: WorldMock
    private lateinit var entity: Entity
    private lateinit var player: PlayerMock

    @BeforeTest fun initialiseEnvironment() {
        world = WorldMock()

        val location = Location(world, 0.0, 0.0, 0.0)
        entity = world.createEntity(location, Sheep::class.java)

        player = server.addPlayer()
    }

    @Test fun `entity should ignite when right clicked with a flint and steel`() {
        player.inventory.setItemInMainHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, entity.location.toVector()).callEvent()

        assertTrue(entity.fireTicks > 0, "Entity should be on fire.")
    }

    @Test fun `entity should ignite when right clicked with a flint and steel in the offhand`() {
        player.inventory.setItemInOffHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, entity.location.toVector()).callEvent()

        assertTrue(entity.fireTicks > 0, "Entity should be on fire.")
    }

    @Test fun `entity should not ignite when offhand is occupied`() {
        player.inventory.setItemInMainHand(ItemStack(Material.FLINT_AND_STEEL))
        player.inventory.setItemInOffHand(ItemStack(Material.DIAMOND))

        PlayerInteractAtEntityEvent(player, entity, entity.location.toVector()).callEvent()

        assertTrue(entity.fireTicks <= 0, "Entity should not be on fire.")
    }

    @Test fun `entity should not ignite when main hand is occupied`() {
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND))
        player.inventory.setItemInOffHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, entity.location.toVector()).callEvent()

        assertTrue(entity.fireTicks <= 0, "Entity should not be on fire.")
    }

    @Test fun `event listener should work`() {
        val event = PlayerInteractAtEntityEvent(player, entity, entity.location.toVector())
        val listener = EntityIgniteListener()

        listener.onEntityIgnite(event)

        assertTrue(entity.fireTicks != 0, "Entity should be on fire.")
    }
}
