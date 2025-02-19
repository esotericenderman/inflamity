package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
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
        player.teleport(world.spawnLocation.add(0.0, 0.0, 3.0))
    }

    @Test fun `players cannot put out fire with their hand`() {
        val fire = world.spawnLocation.block
        fire.type = Material.FIRE

        player.breakBlock(fire)

        assertTrue(player.fireTicks > 0, "Player should be on fire after attempting to put out fire with bare hands.")
        assertEquals(Material.FIRE, fire.type, "Fire should remain after attempting to put out fire with bare hands.")

        server.scheduler.performOneTick()

        assertTrue(player.fireTicks > 0, "Player should be on fire one tick after attempting to put out fire with bare hands.")
        assertEquals(Material.FIRE, fire.type, "Fire should remain one tick after attempting to put out fire with bare hands.")
    }

    @Test fun `non-fire blocks act normally`() {
        for (material in Material.entries) {
            if (material == Material.FIRE) continue

            setUpEnvironment()

            val notFire = world.spawnLocation.block

            try {
                notFire.type = material
            } catch (exception: Exception) {
                continue
            }

            player.breakBlock(notFire)

            assertTrue(player.fireTicks <= 0, "Player should be able to break non-fire blocks without combusting.")
            assertNotEquals(Material.FIRE, notFire.type, "Non-fire block does not turn to fire after player breaks it.")

            server.scheduler.performOneTick()

            assertTrue(player.fireTicks <= 0, "Player should be able to break non-fire blocks without combusting (after one tick).")
            assertNotEquals(Material.FIRE, notFire.type, "Non-fire block does not turn to fire after player breaks it and remains so for the next tick.")
        }
    }
}
