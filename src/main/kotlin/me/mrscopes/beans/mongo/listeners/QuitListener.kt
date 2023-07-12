package me.mrscopes.beans.mongo.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.mongo.Mongo
import me.mrscopes.beans.utilities.mongoPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val mongo = Beans.mongo
        mongo.putPlayerInDatabase(player.mongoPlayer()!!)
        mongo.mongoPlayers.remove(player.uniqueId)
    }
}