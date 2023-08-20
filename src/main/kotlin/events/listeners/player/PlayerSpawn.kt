package events.listeners.player

import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.permission.Permission
import utilities.plainText

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        val player = event.player;
        player.gameMode = GameMode.ADVENTURE
        if (player.name.plainText() == "MrScopes") player.addPermission(Permission("*"))
        println("${event.player.name.plainText()} joined.")
    }
}