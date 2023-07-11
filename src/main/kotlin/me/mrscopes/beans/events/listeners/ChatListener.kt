package me.mrscopes.beans.events.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.mrscopes.beans.Beans
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.plainText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.UUID
import kotlin.collections.HashMap

class ChatListener : Listener {
    val antispam: HashMap<UUID, Long> = HashMap()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncChatEvent) {
        val player = event.player

        if (player.hasPermission("beans.chat.spam")) {
            val uuid = event.player.uniqueId
            val now = System.currentTimeMillis()
            if (antispam.containsKey(uuid)) {
                val lastMessageTime = now - antispam[uuid]!!
                if (lastMessageTime < 1500) {
                    val remaining = ((1500 - lastMessageTime)).toString().split("")
                    var time = "${remaining[1]}.${remaining[2]}${remaining[3]}"
                    if (remaining.size < 6) {
                        time = "0.${remaining[1]}${remaining[2]}"
                    }
                    player.sendMessage("&cWait $time seconds to chat again.".color())
                    event.isCancelled = true
                    return
                }
            }

            antispam[uuid] = now
        }

        var message = event.message()
        val plainText = message.plainText()
        if (player.hasPermission("beans.chat.color")) {
            message = plainText.color()
        }

        if (plainText.contains("[item]")) {
            val item = player.inventory.itemInMainHand
            if (item.type != Material.AIR) {
                val name = item.displayName().hoverEvent(item.asHoverEvent())
                val component = Component.text()
                        .append(Component.text(if (item.amount > 1) item.amount.toString() + "x " else "", item.displayName().color()))
                        .append(name)
                        .build()
                message.replaceText { msg: TextReplacementConfig.Builder -> msg.once().match("\\[item]").replacement(component) }
            }
        }

        event.renderer { player, playerDisplayName, msg, audience ->
                Component.text("${player.name}: $msg")
        }
    }
}
