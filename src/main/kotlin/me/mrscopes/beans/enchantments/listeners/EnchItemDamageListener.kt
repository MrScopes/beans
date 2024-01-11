package me.mrscopes.beans.enchantments.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.enchantments.Enchantments
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent

class EnchItemDamageListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onItemDamage(event: PlayerItemDamageEvent) {
        Enchantments.attempt(listOf("Repairing"), event.item)
    }
}