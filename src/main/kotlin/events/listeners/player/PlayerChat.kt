package events.listeners.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.player.PlayerChatEvent
import utilities.miniMessage
import utilities.plainText

class PlayerChat(event: PlayerChatEvent) {
    init {
        event.setChatFormat { "<gray>${event.player.name.plainText()}<white>: ".miniMessage().append(Component.text(event.message, NamedTextColor.WHITE)) }
        println("${event.player.name.plainText()}: ${event.message}")
    }
}