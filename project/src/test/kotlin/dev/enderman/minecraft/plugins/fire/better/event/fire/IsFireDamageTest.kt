package dev.enderman.minecraft.plugins.fire.better.event.fire

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.events.fire.fireDamageTypes
import dev.enderman.minecraft.plugins.fire.better.events.fire.isFireDamage
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Villager
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsFireDamageTest : AbstractInflamityPluginTest() {
    private lateinit var victim: Villager

    @BeforeTest fun setupVictim() {
        val world = WorldMock()
        victim = world.spawn(world.spawnLocation, Villager::class.java)
    }

    @Test fun `standing in fire is fire damage`() {
        val damage = 1.0
        victim.health -= damage

        val event = EntityDamageEvent(
            victim,
            DamageCause.FIRE,
            DamageSource.builder(DamageType.IN_FIRE).withDamageLocation(victim.location).build(),
            damage
        )

        assertTrue(event.isFireDamage(), "Standing in fire counts as fire damage.")
    }

    @Test fun `being on fire is fire damage`() {
        val damage = 1.0
        victim.health -= damage

        val event = EntityDamageEvent(
            victim,
            DamageCause.FIRE_TICK,
            DamageSource.builder(DamageType.ON_FIRE).withDamageLocation(victim.location).build(),
            damage
        )

        assertTrue(event.isFireDamage(), "Being on fire counts as fire damage.")
    }

    @Test fun `standing in a campfire counts as fire damage`() {
        val damage = 1.0
        victim.health -= damage

        val event = EntityDamageEvent(
            victim,
            DamageCause.CAMPFIRE,
            DamageSource.builder(DamageType.CAMPFIRE).withDamageLocation(victim.location).build(),
            damage
        )

        assertTrue(event.isFireDamage(), "Standing in a campfire counts as fire damage.")
    }

    @Test fun `other damage sources don't count as fire damage`() {
        for (cause in DamageCause.entries) {
            if (cause in fireDamageTypes) continue

            val damage = 1.0
            victim.health -= damage

            val event = EntityDamageEvent(
                victim,
                cause,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(victim.location).build(),
                damage
            )

            assertFalse(event.isFireDamage(), "Standing in a campfire counts as fire damage.")
        }
    }
}