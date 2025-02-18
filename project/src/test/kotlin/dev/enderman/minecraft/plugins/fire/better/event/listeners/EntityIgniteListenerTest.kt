package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.InflamityPlugin
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

class EntityIgniteListenerTest {
    private lateinit var server: ServerMock
    private lateinit var plugin: InflamityPlugin
    private lateinit var world: WorldMock
    private lateinit var entity: Entity
    private lateinit var player: PlayerMock

    @BeforeTest fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.loadWith(InflamityPlugin::class.java, "paper-plugin.yml")
        MockBukkit.ensureMocking()

        world = WorldMock()

        val location = Location(world, 0.0, 0.0, 0.0)

        entity = world.createEntity(location, Sheep::class.java)
        player = server.addPlayer()
    }

    @Test fun onEntityIgniteTest() {
        player.inventory.setItemInMainHand(ItemStack(Material.FLINT_AND_STEEL))

        PlayerInteractAtEntityEvent(player, entity, entity.location.toVector()).callEvent()

        assertTrue(entity.fireTicks != 0, "Entity should be on fire.")
    }

    @AfterTest fun tearDown() {
        MockBukkit.unmock()
    }
}
