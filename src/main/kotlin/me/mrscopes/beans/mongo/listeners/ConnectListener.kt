package me.mrscopes.beans.mongo.listeners

import me.mrscopes.beans.Beans
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class ConnectListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onConnect(event: AsyncPlayerPreLoginEvent) {
        val mongo = Beans.mongo
        val uuid = event.uniqueId
        val player = mongo.playerFromDatabase(uuid)!!
        mongo.mongoPlayers[uuid] = player
        mongo.putPlayerInDatabase(player)
    }
}
