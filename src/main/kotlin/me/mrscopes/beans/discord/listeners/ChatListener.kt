package me.mrscopes.beans.discord.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.mrscopes.beans.Beans
import me.mrscopes.beans.utilities.plainText
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class ChatListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onChat(event: AsyncChatEvent) {
        val player = event.player

        var message = event.message()
        var plainText = message.plainText().replace(Regex("(@everyone|<@538205671712358450>)"), "at everyone").replace("§", "")

        Beans.discord.serverChat.sendMessage("${player.name}: $plainText").queue()
    }
}