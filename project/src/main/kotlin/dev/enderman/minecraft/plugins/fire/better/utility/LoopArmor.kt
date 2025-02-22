package dev.enderman.minecraft.plugins.fire.better.utility

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.BiConsumer
import java.util.function.Consumer

fun Entity.loopArmor(consumer: Consumer<ItemStack>) {
    if (this !is LivingEntity) return

    equipment?.armorContents?.forEach { armor -> consumer.accept(armor) }
}

fun Entity.loopArmorMeta(consumer: BiConsumer<ItemMeta, ItemStack>) {
    loopArmor { armor -> armor.editMeta { consumer.accept(it, armor) } }
}

fun Entity.loopDamageableArmor(consumer: BiConsumer<Damageable, ItemStack>) {
    loopArmorMeta { meta, item -> if (meta is Damageable) consumer.accept(meta, item) }
}
