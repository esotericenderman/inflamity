package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityContactListener
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Creeper
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.mockbukkit.mockbukkit.entity.PlayerMock
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class EntityContactListenerTest : AbstractInflamityPluginTest() {
    private lateinit var world: WorldMock
    private lateinit var player: PlayerMock
    private lateinit var creeper: Creeper

    @BeforeTest fun setUpEnvironment() {
        world = WorldMock()

        player = server.addPlayer()
        player.fireTicks = 10_000

        creeper = world.createEntity(world.spawnLocation, Creeper::class.java)
    }

    @Test fun `fire spreads on attack`() {
        player.attack(creeper)
        assertTrue(creeper.fireTicks > 0, "Creeper should be on fire after being hit by lit player.")
    }

    @Test fun `event listener works`() {
        for (damageType in listOf(DamageType.PLAYER_ATTACK, DamageType.MOB_ATTACK, DamageType.MOB_ATTACK_NO_AGGRO)) {
            val listener = EntityContactListener()

            val cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK
            val source = DamageSource.builder(damageType).withDirectEntity(player).withCausingEntity(player).withDamageLocation(creeper.location).build()

            val event = EntityDamageByEntityEvent(player, creeper, cause, source, 1.0)

            listener.onHit(event)

            assertTrue(creeper.fireTicks > 0, "Creeper should be on fire after being hit by lit player.")
        }
    }

    @Test fun `event listener works with non-lit attacker`() {
        player.fireTicks = 0
        val listener = EntityContactListener()

        val cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK
        val source = DamageSource.builder(DamageType.PLAYER_ATTACK).withDirectEntity(player).withCausingEntity(player).withDamageLocation(creeper.location).build()

        val event = EntityDamageByEntityEvent(player, creeper, cause, source, 1.0)

        listener.onHit(event)

        assertTrue(creeper.fireTicks <= 0, "Creeper should not be on fire after being hit by non-lit player.")
    }

    @Test fun `event listener works with non-direct attack`() {
        player.fireTicks = 0
        val listener = EntityContactListener()

        val cause = EntityDamageEvent.DamageCause.CUSTOM
        val source = DamageSource.builder(DamageType.GENERIC).withDirectEntity(player).withCausingEntity(player).withDamageLocation(creeper.location).build()

        val event = EntityDamageByEntityEvent(player, creeper, cause, source, 1.0)

        listener.onHit(event)

        assertTrue(creeper.fireTicks <= 0, "Creeper should not be on fire after being hit by non-direct attack.")
    }
}
