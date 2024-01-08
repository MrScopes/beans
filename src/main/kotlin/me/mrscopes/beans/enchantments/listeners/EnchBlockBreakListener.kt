package me.mrscopes.beans.enchantments.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.regions.WorldGuard
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class EnchBlockBreakListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (WorldGuard.isLocationGuarded(event.block.location)) return

        Beans.enchantments.tryToActivate(event.player.inventory.itemInMainHand, event.player)
    }
}