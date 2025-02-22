package dev.enderman.minecraft.plugins.fire.better.utility

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

fun Entity.loopArmor(consumer: Consumer<ItemStack>) {
    if (this !is LivingEntity) return

    equipment?.armorContents?.forEach { armor -> consumer.accept(armor) }
}
