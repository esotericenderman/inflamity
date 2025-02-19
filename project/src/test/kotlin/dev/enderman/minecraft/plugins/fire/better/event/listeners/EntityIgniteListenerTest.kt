package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Sheep
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.mockbukkit.mockbukkit.entity.PlayerMock
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.*

class EntityIgniteListenerTest : AbstractInflamityPluginTest() {
    private lateinit var world: WorldMock
    private lateinit var entity: Entity
    private lateinit var player: PlayerMock

    @BeforeTest fun initialiseEnvironment() {
        world = WorldMock()
        entity = world.createEntity(world.spawnLocation, Sheep::class.java)
        player = server.addPlayer()
    }

    @Test fun `entity ignites when right-clicked with a flint and steel (event)`() {
        player.inventory.setItemInMainHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, Vector()).callEvent()

        assertTrue(entity.fireTicks > 0, "Entity should be on fire.")
    }

    @Test fun `entity ignites when right-clicked with a flint and steel in the offhand (event)`() {
        player.inventory.setItemInOffHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, Vector()).callEvent()

        assertTrue(entity.fireTicks > 0, "Entity should be on fire.")
    }

    @Test fun `entity does not ignite when offhand is occupied (event)`() {
        player.inventory.setItemInMainHand(ItemStack(Material.FLINT_AND_STEEL))
        player.inventory.setItemInOffHand(ItemStack(Material.DIAMOND))

        PlayerInteractAtEntityEvent(player, entity, Vector()).callEvent()

        assertTrue(entity.fireTicks <= 0, "Entity should not be on fire.")
    }

    @Test fun `entity does not ignite when main hand is occupied (event)`() {
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND))
        player.inventory.setItemInOffHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, Vector()).callEvent()

        assertTrue(entity.fireTicks <= 0, "Entity should not be on fire.")
    }

    @Test fun `entity does not ignite when holding two flint and steels (event)`() {
        player.inventory.setItemInMainHand(ItemStack(Material.FLINT_AND_STEEL))
        player.inventory.setItemInOffHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, Vector()).callEvent()

        assertTrue(entity.fireTicks <= 0, "Entity should not be on fire.")
    }

    @Test fun `entity does not ignite when not holding a flint and steel (event)`() {
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND))

        PlayerInteractAtEntityEvent(player, entity, Vector()).callEvent()

        assertTrue(entity.fireTicks <= 0, "Entity should not be on fire.")
    }
}
