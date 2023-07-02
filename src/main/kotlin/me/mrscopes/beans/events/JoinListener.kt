package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import me.mrscopes.beans.color
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        event.joinMessage("&8[&a+&8] &7${player.name}".color())
        Beans.discord.serverChat.sendMessage("✅ **${player.name}** joined the server.").queue()
    }
}
