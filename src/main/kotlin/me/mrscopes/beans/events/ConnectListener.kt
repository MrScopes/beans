package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class ConnectListener : Listener {
    @EventHandler
    fun onConnect(event: AsyncPlayerPreLoginEvent) {
        val mongo = Beans.mongo
        val uuid = event.uniqueId
        var player = mongo.playerFromDatabase(uuid)
        mongo.mongoPlayers[uuid] = player
        mongo.putPlayerInDatabase(player)
    }
}
