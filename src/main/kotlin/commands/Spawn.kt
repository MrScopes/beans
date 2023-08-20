package commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.entity.Player

class Spawn : Command("spawn") {
    init {
        defaultExecutor = CommandExecutor { sender: CommandSender, _: CommandContext? ->
            val player = sender as Player
            player.sendMessage("Teleporting to spawn...")
            player.teleport(Server.spawn)
        }
    }
}