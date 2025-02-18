package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.events.listeners.EntityBurnListener
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
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

        player.location.block.type = Material.FIRE

        server.scheduler.runTaskLater(plugin, { -> assertTrue(player.fireTicks > 0)}, 10_000L)
    }

    @Test fun `suffocating entity stops burning`() {
        player.location.block.type = Material.FIRE

        server.scheduler.runTaskLater(plugin, { ->
            assertTrue(player.fireTicks > 0)

            player.location.block.type = Material.SAND
            player.location.block.getRelative(BlockFace.UP).type = Material.SAND

            server.scheduler.runTaskLater(plugin, { -> assertTrue(player.fireTicks <= 0)}, 20L)
        }, 50L);
    }

    @Test fun `event listener works`() {
        val event = EntityDamageEvent(player, EntityDamageEvent.DamageCause.FIRE, DamageSource.builder(DamageType.ON_FIRE).build(), 1.0)
        val listener = EntityBurnListener()

        listener.onEntityBurn(event)

        assertTrue(player.fireTicks > 0)
    }

    @Test fun `event listener works for non-fire damage`() {
        val event = EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, DamageSource.builder(DamageType.GENERIC).build(), 1.0)
        val listener = EntityBurnListener()

        listener.onEntityBurn(event)

        assertTrue(player.fireTicks <= 0)
    }

    @Test fun `event listener works for suffocation`() {
        player.fireTicks = 10_000

        val event = EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUFFOCATION, DamageSource.builder(DamageType.IN_WALL).build(), 1.0)
        val listener = EntityBurnListener()

        listener.onEntityBurn(event)

        assertTrue(player.fireTicks <= 0)
    }
}
