package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.events.fire.fireDamageTypes
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Sheep
import org.bukkit.event.entity.EntityDamageEvent
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EntityFireSpreadListenerTest : AbstractInflamityPluginTest() {
    private lateinit var world: World
    private lateinit var entity: LivingEntity
    private lateinit var startingBlock: Block

    @BeforeTest fun setUpEnvironment() {
        world = WorldMock()
        entity = world.createEntity(world.spawnLocation, Sheep::class.java)
        startingBlock = entity.location.block
        startingBlock.getRelative(BlockFace.DOWN).type = Material.IRON_BLOCK
    }

    @Test fun `lit entities spread fire (event)`() {
        for (damageType in fireDamageTypes) {
            setUpEnvironment()

            val damage = 1.0
            entity.damage(damage)

            val event = EntityDamageEvent(
                entity,
                damageType,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(entity.location).build(),
                damage
            )
            event.callEvent()

            server.scheduler.performTicks(100L)

            assertEquals(Material.FIRE, startingBlock.type, "Lit entity should spread fire after being damaged by fire.")
        }
    }

    @Test fun `unlit entities do not spread fire`() {
        server.scheduler.performTicks(100L)

        assertNotEquals(Material.FIRE, startingBlock.type)
    }

    @Test fun `non-fire damage does not spread fire (event)`() {
        val event = EntityDamageEvent(
            entity,
            EntityDamageEvent.DamageCause.SUICIDE,
            DamageSource.builder(DamageType.GENERIC).build(),
            1.0
        )

        event.callEvent()

        assertNotEquals(Material.FIRE, startingBlock.type, "Block should not be on fire when entity is not on fire.")
    }

    @Test fun `lit entity does not spread fire if block is not air (event)`() {
        for (damageType in fireDamageTypes) {
            setUpEnvironment()
            startingBlock.type = Material.SHORT_GRASS

            val damage = 1.0
            entity.damage(damage)

            val event = EntityDamageEvent(
                entity,
                damageType,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(entity.location).build(),
                damage
            )
            event.callEvent()

            assertNotEquals(Material.FIRE, startingBlock.type, "Lit entity should not spread fire to non-air block.")

            server.scheduler.performTicks(100L)

            assertNotEquals(Material.FIRE, startingBlock.type, "Lit entity should not spread fire to non-air block.")
        }
    }

    @Test fun `lit entity does not spread fire mid-air (event)`() {
        for (damageType in fireDamageTypes) {
            setUpEnvironment()
            startingBlock.getRelative(BlockFace.DOWN).type = Material.AIR

            val damage = 1.0
            entity.damage(damage)

            val event = EntityDamageEvent(
                entity,
                damageType,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(entity.location).build(),
                damage
            )
            event.callEvent()

            assertNotEquals(Material.FIRE, startingBlock.type, "Lit entity should not spread fire while mid-air.")

            server.scheduler.performTicks(100L)

            assertNotEquals(Material.FIRE, startingBlock.type, "Lit entity should not spread fire while mid-air.")
        }
    }
}
