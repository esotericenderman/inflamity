package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class EntityBurnListenerTest : AbstractInflamityPluginTest() {
    private lateinit var player: Player
    private lateinit var world: WorldMock

    @BeforeTest fun setUpEnvironment() {
        world = WorldMock()
        player = server.addPlayer()
    }

    @Test fun `entity that starts burning burns forever`() {
        player.addPotionEffect(
            PotionEffect(
                PotionEffectType.REGENERATION,
                PotionEffect.INFINITE_DURATION,
                5
            )
        )

        player.fireTicks = 10_000

        server.scheduler.performTicks(50_000L)

        assertTrue(player.fireTicks > 0, "Player that starting burning a long time ago does not stop burning.")
    }

    @Test fun `suffocating entity stops burning`() {
        player.fireTicks = 10_000

        server.scheduler.performTicks(50_000L)

        assertTrue(player.fireTicks > 0, "Player is burning after being set on fire a long time ago.")

        player.location.block.type = Material.SAND
        player.location.block.getRelative(BlockFace.UP).type = Material.SAND

        player.damage(1.0, DamageSource.builder(DamageType.IN_WALL).withDamageLocation(player.location).build())

        assertTrue(player.fireTicks <= 0, "Player stops burning after being suffocated (no oxygen for the fire to burn).")
    }

    @Test fun `suffocating entity stops burning (event)`() {
        player.fireTicks = 10_000

        server.scheduler.performTicks(50_000L)

        assertTrue(player.fireTicks > 0, "Player is burning after being set on fire a long time ago.")

        player.location.block.type = Material.SAND
        player.location.block.getRelative(BlockFace.UP).type = Material.SAND

        val damage = 1.0

        player.damage(damage)
        val event = EntityDamageEvent(
            player,
            EntityDamageEvent.DamageCause.SUFFOCATION,
            DamageSource.builder(DamageType.IN_WALL).withDamageLocation(player.location).build(),
            damage
        )

        event.callEvent()

        assertTrue(player.fireTicks <= 0, "Player stops burning after being suffocated (no oxygen for the fire to burn).")
    }
}
