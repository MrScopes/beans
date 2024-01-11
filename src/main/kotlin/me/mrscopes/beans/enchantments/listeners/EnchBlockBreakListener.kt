package me.mrscopes.beans.enchantments.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.enchantments.Enchantments
import me.mrscopes.beans.regions.WorldGuard
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class EnchBlockBreakListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!listOf(Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE).contains(event.player.inventory.itemInMainHand.type)) return
        if (WorldGuard.isLocationGuarded(event.block.location)) return

        Enchantments.attempt(event.player.inventory.itemInMainHand, event.player, event.block)
    }
}