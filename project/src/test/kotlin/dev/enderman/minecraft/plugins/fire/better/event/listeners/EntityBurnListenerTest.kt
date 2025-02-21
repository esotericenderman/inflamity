package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.events.fire.fireDamageTypes
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.block.BlockFace
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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

    @Test fun `entity that starts burning burns forever (event)`() {
        player.addPotionEffect(
            PotionEffect(
                PotionEffectType.REGENERATION,
                PotionEffect.INFINITE_DURATION,
                5
            )
        )

        val damage = 1.0
        player.damage(damage)
        val event = EntityDamageEvent(
            player,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            DamageSource.builder(DamageType.ON_FIRE).withDamageLocation(player.location).build(),
            damage
        )

        event.callEvent()

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

    @Test fun `non-fire damage does not set entities on fire (event)`() {
        for (damageCause in EntityDamageEvent.DamageCause.entries) {
            if (damageCause in fireDamageTypes) continue

            setUpEnvironment()

            val damage = 1.0

            player.damage(damage)
            val event = EntityDamageEvent(
                player,
                EntityDamageEvent.DamageCause.SUICIDE,
                DamageSource.builder(DamageType.GENERIC_KILL).withDamageLocation(player.location).withDirectEntity(player).withCausingEntity(player).build(),
                damage
            )

            event.callEvent()

            assertTrue(player.fireTicks <= 0, "Generic damage does not set entities on fire.")
        }
    }

    @Test fun `entity with full fire protection is immune to fire`() {
        for (damageCause in fireDamageTypes) {
            setUpEnvironment()

            val helmet = ItemStack(Material.DIAMOND_HELMET)
            val chest = ItemStack(Material.DIAMOND_CHESTPLATE)
            val leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            val boots = ItemStack(Material.DIAMOND_BOOTS)

            val itemDamage = 5

            val equipment = listOf(helmet, chest, leggings, boots)
            for (item in equipment) {
                item.addEnchantment(Enchantment.FIRE_PROTECTION, 4)

                item.editMeta(Damageable::class.java) {
                    it.damage = itemDamage
                }
            }

            val playerEquipped = player.equipment
            playerEquipped.helmet = helmet
            playerEquipped.chestplate = chest
            playerEquipped.leggings = leggings
            playerEquipped.boots = boots

            val damage = 1.0

            val event = EntityDamageEvent(
                player,
                damageCause,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(player.location).withDirectEntity(player).withCausingEntity(player).build(),
                damage
            )

            event.callEvent()

            if (!event.isCancelled) {
                player.damage(damage)
            }

            assertTrue(player.fireTicks <= 0, "Player is not set on fire if they have full fire protection.")

            val maxHealth = player.getAttribute(Attribute.MAX_HEALTH)!!.value

            assertEquals(maxHealth, player.health, "Player should be on full health with full fire protection.")

            for (item in equipment) {
                assertEquals(itemDamage, (item.itemMeta as Damageable).damage, "Item damage should not change from fire with maximum fire protection.")
            }
        }
    }
}
