package dev.enderman.minecraft.plugins.fire.better.enchantments.fire.protection

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

fun Entity.getFireProtectionLevel(): Int {
    if (this !is LivingEntity) return 0

    var total = 0

    equipment?.armorContents?.filterNotNull()?.forEach { total += it.getEnchantmentLevel(Enchantment.FIRE_PROTECTION) }

    return total
}

fun Entity.getMaxFireProtectionLevel(): Int {
    if (this !is LivingEntity) return 0

    return Enchantment.FIRE_PROTECTION.maxLevel * (equipment?.armorContents?.size ?: 0)
}

fun Entity.getFireProtectionFactor(): Double {
    if (this !is LivingEntity) return 0.0

    return getFireProtectionLevel() / getMaxFireProtectionLevel().toDouble()
}

fun ItemStack.getFireProtectionLevel(): Int {
    return getEnchantmentLevel(Enchantment.FIRE_PROTECTION)
}

fun ItemStack.getFireProtectionFactor(): Double {
    return getFireProtectionLevel() / Enchantment.FIRE_PROTECTION.maxLevel.toDouble()
}
