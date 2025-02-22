package dev.enderman.minecraft.plugins.fire.better.utility.armor

import org.apache.commons.lang3.function.TriConsumer
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

fun Entity.loopArmorItemsIndexed(consumer: BiConsumer<ItemStack, Int>) {
    var index = -1
    loopArmorItems { consumer.accept(it, ++index) }
}

fun Entity.loopArmorMeta(consumer: Consumer<ItemMeta>) {
    loopArmorItems { consumer.accept(it.itemMeta) }
}

fun Entity.loopArmorMetaIndexed(consumer: BiConsumer<ItemMeta, Int>) {
    var index = -1
    loopArmorMeta { consumer.accept(it, ++index) }
}

fun Entity.loopArmor(consumer: BiConsumer<ItemMeta, ItemStack>) {
    loopArmorItems { it.editMeta { meta -> consumer.accept(meta, it) } }
}

fun Entity.loopArmorIndexed(consumer: TriConsumer<ItemMeta, ItemStack, Int>) {
    var index = -1
    loopArmor { meta, item -> consumer.accept(meta, item, ++index) }
}

fun Entity.loopDamageableArmorItems(consumer: Consumer<ItemStack>) {
    loopArmorItems { if (it.itemMeta is Damageable) consumer.accept(it) }
}

fun Entity.loopDamageableArmorItemsIndexed(consumer: BiConsumer<ItemStack, Int>) {
    var index = -1
    loopDamageableArmorItems { consumer.accept(it, ++index) }
}

fun Entity.loopDamageableArmorMeta(consumer: Consumer<ItemMeta>) {
    loopDamageableArmorItems { it.editMeta(Damageable::class.java) { meta -> consumer.accept(meta) } }
}

fun Entity.loopDamageableArmorMetaIndexed(consumer: BiConsumer<ItemMeta, Int>) {
    var index = -1
    loopDamageableArmorMeta { consumer.accept(it, ++index) }
}

fun Entity.loopDamageableArmor(consumer: BiConsumer<Damageable, ItemStack>) {
    loopDamageableArmorItems { it.editMeta(Damageable::class.java) { meta -> consumer.accept(meta, it) } }
}

fun Entity.loopDamageableArmorIndexed(consumer: TriConsumer<Damageable, ItemStack, Int>) {
    var index = -1
    loopDamageableArmor { meta, item -> consumer.accept(meta, item, ++index) }
}
