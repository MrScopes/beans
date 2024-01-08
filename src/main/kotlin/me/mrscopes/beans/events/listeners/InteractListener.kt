package me.mrscopes.beans.events.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector

class InteractListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.clickedBlock?.type == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            event.player.velocity = Vector(50, 5, 50)
        }
    }
}