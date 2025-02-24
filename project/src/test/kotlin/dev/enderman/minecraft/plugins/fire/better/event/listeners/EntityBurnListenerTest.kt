package dev.enderman.minecraft.plugins.fire.better.event.listeners

import dev.enderman.minecraft.plugins.fire.better.AbstractInflamityPluginTest
import dev.enderman.minecraft.plugins.fire.better.FIRE_DURATION
import dev.enderman.minecraft.plugins.fire.better.events.fire.fireDamageTypes
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.block.BlockFace
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockbukkit.mockbukkit.world.WorldMock
import kotlin.math.pow
import kotlin.random.Random
import kotlin.test.*

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

        player.fireTicks = FIRE_DURATION

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
        player.fireTicks = FIRE_DURATION

        server.scheduler.performTicks(50_000L)

        assertTrue(player.fireTicks > 0, "Player is burning after being set on fire a long time ago.")

        player.location.block.type = Material.SAND
        player.location.block.getRelative(BlockFace.UP).type = Material.SAND

        player.damage(1.0, DamageSource.builder(DamageType.IN_WALL).withDamageLocation(player.location).build())

        assertTrue(player.fireTicks <= 0, "Player stops burning after being suffocated (no oxygen for the fire to burn).")
    }

    @Test fun `suffocating entity stops burning (event)`() {
        player.fireTicks = FIRE_DURATION

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

    @Test fun `non-living entity is set on fire`() {
        for (cause in fireDamageTypes) {
            val nonLiving = world.spawn(world.spawnLocation, Snowball::class.java)

            val damage = 1.0

            val event = EntityDamageEvent(
                nonLiving,
                cause,
                DamageSource.builder(DamageType.ON_FIRE).withDamageLocation(nonLiving.location).build(),
                damage
            )

            event.callEvent()

            assertEquals(FIRE_DURATION, nonLiving.fireTicks)
        }
    }

    @Test fun `non-full armor partially protects from fire`() {
        for (cause in fireDamageTypes) {
            setUpEnvironment()

            val helm = ItemStack(Material.IRON_HELMET)
            val chest = ItemStack(Material.IRON_CHESTPLATE)
            val leg = ItemStack(Material.IRON_LEGGINGS)
            val boot = ItemStack(Material.IRON_BOOTS)

            val itemDamage = 10

            val plating = listOfNotNull(helm, chest, leg, boot)
            for (plate in plating) {
                plate.addEnchantment(Enchantment.FIRE_PROTECTION, 2)
                plate.editMeta(Damageable::class.java) {
                    it.damage = itemDamage
                }
            }

            val equipped = player.equipment
            equipped.helmet = helm
            equipped.chestplate = chest
            equipped.leggings = leg
            equipped.boots = boot

            val damage = 10.0

            val event = EntityDamageEvent(
                player,
                cause,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(player.location).build(),
                damage
            )

            val originalFinalDamage = event.finalDamage

            event.callEvent()

            assertFalse(event.isCancelled, "Event should not be cancelled with non-full fire protection.")

            for (item in plating) {
                val meta = item.itemMeta as Damageable
                val oneLessDurable = itemDamage + 1

                assertTrue(meta.damage == oneLessDurable || meta.damage == itemDamage, "Item durability should either stay the same or decrease by one with non-full fire protection.")
            }

            val newFinalDamage = event.finalDamage

            assertTrue(newFinalDamage < originalFinalDamage, "Damage should be reduced when wearing partial fire protection.")
        }
    }

    @Test fun `equipping weird items does not throw errors`() {
        for (cause in fireDamageTypes) {
            val pumpkin = ItemStack(Material.PUMPKIN)

            player.equipment.helmet = pumpkin

            val damage = 1.0

            val event = EntityDamageEvent(
                player,
                cause,
                DamageSource.builder(DamageType.GENERIC).withDamageLocation(player.location).build(),
                damage
            )

            event.callEvent()

            assertFalse(event.isCancelled, "Event should not be cancelled when wearing weird items.")
        }
    }

    @Test fun `fire protection works with partially equipped armor`() {
        val armor =  listOf(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)

        for (cause in fireDamageTypes) {
            for (i in 1..<(2.0.pow(armor.size.toDouble()).toInt())) {
                val bitmap = i.toString(2).padStart(4, '0').split("").filter { s -> s.isNotBlank() }.map(String::toInt).map({ x -> x == 1 })

                val itemDamage = Random.nextInt(5)

                armor.forEachIndexed { index, armour ->
                    if (!bitmap[index]) return@forEachIndexed

                    setUpEnvironment()

                    val item = ItemStack(armour)

                    item.editMeta(Damageable::class.java) {
                        it.damage = itemDamage
                    }

                    when (index) {
                        0 -> player.equipment.helmet = item
                        1 -> player.equipment.chestplate = item
                        2 -> player.equipment.boots = item
                        3 -> player.equipment.boots = item
                    }
                }

                val damage = 1.0

                val event = EntityDamageEvent(
                    player,
                    cause,
                    DamageSource.builder(DamageType.GENERIC).withDamageLocation(player.location).build(),
                    damage
                )

                val originalFinalDamage = event.finalDamage

                event.callEvent()

                val newFinalDamage = event.finalDamage

                assertTrue(newFinalDamage <= originalFinalDamage, "Damage should be reduced or stay the same depending on fire protection level.")
                assertFalse(event.isCancelled, "Event should not be cancelled with no fire protection.")

                val newArmor = player.equipment.armorContents.filterNotNull()
                newArmor.forEach {
                    piece -> assertEquals(itemDamage, (piece.itemMeta as Damageable).damage, "Durability should decrease when taking fire damage.")
                }
            }
        }
    }
}
