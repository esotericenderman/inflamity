package dev.enderman.minecraft.plugins.fire.better.utility.armor

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.BiConsumer
import java.util.function.Consumer

fun Entity.loopArmorItems(consumer: Consumer<ItemStack>) {
    if (this !is LivingEntity) return

    equipment?.armorContents?.forEach { consumer.accept(it) }
}

fun Entity.loopArmorMeta(consumer: Consumer<ItemMeta>) {
    loopArmorItems { consumer.accept(it.itemMeta) }
}

fun Entity.loopArmor(consumer: BiConsumer<ItemMeta, ItemStack>) {
    loopArmorItems { it.editMeta { meta -> consumer.accept(meta, it) } }
}

fun Entity.loopDamageableArmorItems(consumer: Consumer<ItemStack>) {
    loopArmorItems { if (it.itemMeta is Damageable) consumer.accept(it) }
}

fun Entity.loopDamageableArmorMeta(consumer: Consumer<ItemMeta>) {
    loopDamageableArmorItems { it.editMeta(Damageable::class.java) { meta -> consumer.accept(meta) } }
}

fun Entity.loopDamageableArmor(consumer: BiConsumer<Damageable, ItemStack>) {
    loopDamageableArmorItems { it.editMeta(Damageable::class.java) { meta -> consumer.accept(meta, it) } }
}
