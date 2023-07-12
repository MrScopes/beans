package me.mrscopes.beans.events.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.mongoPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        event.joinMessage("&8[&a+&8] &7${player.name}".color())
        Beans.discord.serverChat.sendMessage("✅ **${player.name}** joined the server.").queue()
        Utilities.whileLoop({ player.isOnline }, { player.sendActionBar("&2Hello, ${player.name}. You have ${Utilities.moneyFormat(player.mongoPlayer()!!.money)}".color()) }, 20)
    }
}
