package me.mrscopes.beans.events.listeners

import me.mrscopes.beans.Beans
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RespawnListener : Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        event.respawnLocation = Beans.instance.config.getLocation("spawn")!!
    }
}
