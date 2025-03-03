package dev.enderman.minecraft.plugins.fire.better.entity.contact

import org.bukkit.event.entity.EntityDamageEvent.DamageCause

val contactAttacks = listOfNotNull(
    DamageCause.ENTITY_ATTACK,
    DamageCause.ENTITY_SWEEP_ATTACK,
    DamageCause.CRAMMING,
)
