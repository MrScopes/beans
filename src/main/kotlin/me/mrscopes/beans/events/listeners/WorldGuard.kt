package me.mrscopes.beans.events.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.plainText
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BreakListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player

        if (player.hasPermission("beans.donator")) {
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
        if (player.hasPermission("beans.donator")) {
            message = plainText.color()
        }

        /*

        if (plainText.contains("[item]")) {
            val item = player.inventory.itemInMainHand
            if (item.type != Material.AIR) {
                val name = item.displayName().hoverEvent(item.asHoverEvent())
                val component = Component.text()
                    .append(
                        Component.text(
                            if (item.amount > 1) item.amount.toString() + "x " else "",
                            item.displayName().color()
                        )
                    )
                    .append(name)
                    .build()
                message.replaceText { msg: TextReplacementConfig.Builder ->
                    msg.once().match("\\[item]").replacement(component)
                }
            }
        }

         */

        event.renderer { source, sourceDisplayName, msg, audience ->
            return@renderer Component.text("${source.name}: ${msg.plainText()}")
        }
    }
}