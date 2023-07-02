package me.mrscopes.beans.events

import io.papermc.paper.event.player.AsyncChatEvent
import me.mrscopes.beans.Beans
import me.mrscopes.beans.color
import me.mrscopes.beans.plainText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.UUID
import kotlin.collections.HashMap

class ChatListener : Listener {
    val antispam: HashMap<UUID, Long> = HashMap()

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true

        val player = event.player

        if (player.hasPermission("beans.chat.spam")) {
            val uuid = event.player.uniqueId
            val now = System.currentTimeMillis()
            if (antispam.containsKey(uuid)) {
                val lastMessageTime = now - antispam[uuid]!!
                if (lastMessageTime < 2000) {
                    val remaining = ((2000 - lastMessageTime)).toString().split("")
                    var time = "${remaining[1]}.${remaining[2]}${remaining[3]}"
                    if (remaining.size < 6) {
                        time = "0.${remaining[1]}${remaining[2]}"
                    }
                    player.sendMessage("&cWait $time seconds to chat again.".color())
                    return
                }
            }

            antispam[uuid] = now
        }

        var message = event.message()
        var plainText = message.plainText()
        if (player.hasPermission("beans.chat.color"))
            message = plainText.color()

        if (plainText.contains("[item]")) {
            val item = player.inventory.itemInMainHand
            if (item.type != Material.AIR) {
                val name = item.displayName().hoverEvent(item.asHoverEvent())
                val component = Component.text()
                        .append(Component.text(if (item.amount > 1) item.amount.toString() + "x " else "", item.displayName().color()))
                        .append(name)
                        .build()
                message = message.replaceText { msg: TextReplacementConfig.Builder -> msg.once().match("\\[item]").replacement(component) }
            }
        }

        Bukkit.broadcast("&7${event.player.name}&f: $message".color())

        val discordMessage = message.replaceText { builder: TextReplacementConfig.Builder -> builder.match("@everyone").match("<@538205671712358450>").replacement("at everyone") }
        Beans.discord.serverChat.sendMessage("${event.player.name}: ${discordMessage.plainText()}").queue()
    }
}
