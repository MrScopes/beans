package me.mrscopes.beans.regions

import com.sk89q.worldedit.math.BlockVector3
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntitySpawnEvent

class WorldGuard : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(event: BlockBreakEvent) { event.isCancelled = isLocationGuarded(event.player, event.block.location) }
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlace(event: BlockPlaceEvent) { event.isCancelled = isLocationGuarded(event.player, event.block.location) }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onSpawn(event: EntitySpawnEvent) { event.isCancelled = isLocationGuarded(event.location) }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(event: EntityDamageEvent) { event.isCancelled = isLocationGuarded(event.entity.location) }

    companion object {
        fun isLocationGuarded(player: Player, location: Location): Boolean {
            if (player.gameMode == GameMode.CREATIVE) return false

            return Regions.spawnRegion.contains(BlockVector3.at(location.x, location.y, location.z)) || location.y < 20
        }

        fun isLocationGuarded(location: Location): Boolean {
            return (Regions.spawnRegion.contains(BlockVector3.at(location.x, location.y, location.z)) || location.y < 20)
        }
    }
}