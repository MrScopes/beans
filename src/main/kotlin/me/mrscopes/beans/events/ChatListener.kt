package me.mrscopes.beans.events

import io.papermc.paper.event.player.AsyncChatEvent
import me.mrscopes.beans.Beans
import me.mrscopes.beans.plainText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true
        var message = event.message()
        if (message.toString().contains("[item]")) {
            val item = event.player.inventory.itemInMainHand
            if (item.type != Material.AIR) {
                val name = item.displayName().hoverEvent(item.asHoverEvent())
                val component = Component.text()
                        .append(Component.text(if (item.amount > 1) item.amount.toString() + "x " else "", item.displayName().color()))
                        .append(name)
                        .build()
                message = message.replaceText { msg: TextReplacementConfig.Builder -> msg.once().match("\\[item]").replacement(component) }
            }
        }

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<gray>${event.player.name}<white>: <message>",
                Placeholder.component("message", message)))

        val discordMessage = message.replaceText { builder: TextReplacementConfig.Builder -> builder.match("@everyone").match("<@538205671712358450>").replacement("at everyone") }
        Beans.discord.serverChat.sendMessage("${event.player.name}: ${discordMessage.plainText()}").queue()
    }
}
