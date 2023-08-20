package events.listeners.player

import net.minestom.server.event.player.PlayerCommandEvent
import utilities.plainText

class PlayerCommand(event: PlayerCommandEvent) {
    init {
        println("${event.player.name.plainText()} ran \"/${event.command}\"")
    }
}