package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityFireSpreadListener
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.Sheep
import org.bukkit.event.entity.EntityDamageEvent
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EntityFireSpreadListenerTest : AbstractInflamityPluginTest() {
    private lateinit var world: World
    private lateinit var entity: Entity
    private lateinit var startingBlock: Block

    @BeforeTest fun setUpEnvironment() {
        world = WorldMock()
        entity = world.createEntity(world.spawnLocation, Sheep::class.java)
        startingBlock = entity.location.block
    }

    @Test fun `lit entities spread fire`() {
        entity.fireTicks = 100

        server.scheduler.runTaskLater(plugin, { ->
            assertEquals(startingBlock.type, Material.FIRE)
        }, 100L)
    }

    @Test fun `unlit entities do not spread fire`() {
        server.scheduler.runTaskLater(plugin, { ->
            assertEquals(startingBlock.type, Material.FIRE)
        }, 100L)
    }

    @Test fun `event listener works`() {
        assertDoesNotThrow("Event listener should not throw any errors.") {
            val listener = EntityFireSpreadListener()

            val event = EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FIRE, DamageSource.builder(DamageType.ON_FIRE).build(), 1.0)

            listener.onEntityBurn(event)
        }
    }

    @Test fun `event listener works for non-fire damage`() {
        assertDoesNotThrow("Event listener should not throw any errors when handling non-fire damage.") {
            val listener = EntityFireSpreadListener()

            val event = EntityDamageEvent(entity, EntityDamageEvent.DamageCause.SUICIDE, DamageSource.builder(DamageType.GENERIC).build(), 1.0)

            listener.onEntityBurn(event)

            assertNotEquals(Material.FIRE, startingBlock.type, "Block should not be on fire when entity is not on fire.")
        }
    }
}
