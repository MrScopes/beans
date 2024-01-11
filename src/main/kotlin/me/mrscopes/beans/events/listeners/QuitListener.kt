package me.mrscopes.beans.events.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.utilities.color
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage("&8[&c-&8] &7${player.name}".color())
        Beans.events.chatListener.antispam.remove(player.uniqueId)
    }
}