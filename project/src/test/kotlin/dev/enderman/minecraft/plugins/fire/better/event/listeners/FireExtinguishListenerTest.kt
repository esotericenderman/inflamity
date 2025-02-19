package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.junit.jupiter.api.BeforeEach
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class FireExtinguishListenerTest : AbstractInflamityPluginTest() {
    private lateinit var world: WorldMock
    private lateinit var player: Player

    @BeforeEach fun setUpEnvironment() {
        world = WorldMock()

        player = server.addPlayer()
        player.teleport(world.spawnLocation)
    }

    @Test fun `players cannot put out fire with their hand`() {
        val fire = world.getBlockAt(Location(world, 0.0, 0.0, 0.0))
        fire.setType(Material.FIRE)

        player.breakBlock(fire)

        server.scheduler.performOneTick()

        assertTrue(player.fireTicks > 0, "Player should be on fire after attempting to put out fire with bare hands.")
        assertEquals(Material.FIRE, fire.type, "Fire should remain after attempting to put out fire with bare hands.")
    }

    @Test fun `non-fire blocks act normally`() {
        for (material in Material.entries) {
            if (material == Material.FIRE) continue

            val notFire = world.spawnLocation.block

            try {
                notFire.type = material
                player.breakBlock(notFire)
            } catch (exception: Exception) {
                continue
            }

            assertTrue(player.fireTicks <= 0, "Player should be able to break non-fire blocks without combusting.")
            assertNotEquals(Material.FIRE, notFire.type, "Non-fire block does not turn to fire after player breaks it.")
        }
    }
}
