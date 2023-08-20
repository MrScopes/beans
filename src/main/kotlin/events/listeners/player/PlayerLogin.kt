package events.listeners.player

import Server
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerLoginEvent
import utilities.plainText

class PlayerLogin(event: PlayerLoginEvent) {
    init {
        println("${event.player.name.plainText()} logging in...")
        event.setSpawningInstance(Server.hub)
        event.player.respawnPoint = Pos(-99.0, 61.0, -99.0, -44f, 3f)

        Server.hub.sendMessage(Component.text("${event.player.name.plainText()} joined."))
    }
}