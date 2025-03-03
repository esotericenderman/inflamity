package dev.enderman.minecraft.plugins.fire.better.armor

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

fun Entity.loopEquipmentItems(lambda: (ItemStack) -> Unit) {
    loopArmorItems(lambda)

    if (this !is LivingEntity) return
    if (equipment == null) return

    lambda(equipment!!.itemInMainHand)
    lambda(equipment!!.itemInOffHand)
}

fun Entity.loopDamageableEquipmentItems(lambda: (ItemStack) -> Unit) {
    loopDamageableArmorItems(lambda)

    if (this !is LivingEntity) return
    if (equipment == null) return

    val main = equipment!!.itemInMainHand
    val off = equipment!!.itemInOffHand

    if (main.itemMeta is Damageable) {
        lambda(main)
    }

    if (off.itemMeta is Damageable) {
        lambda(off)
    }
}

fun Entity.loopDamageableEquipmentMeta(lambda: (Damageable) -> Unit) {
    loopDamageableEquipmentItems { it.editMeta { meta -> lambda(meta as Damageable) } }
}
