package events.listeners.player

import Server
import net.kyori.adventure.text.Component
import net.minestom.server.event.player.PlayerLoginEvent
import utilities.plainText

class PlayerLogin(event: PlayerLoginEvent) {
    init {
        event.setSpawningInstance(Server.instance)
        event.player.respawnPoint = Server.spawn

        Server.instance.sendMessage(Component.text("${event.player.name.plainText()} joined."))
    }
}