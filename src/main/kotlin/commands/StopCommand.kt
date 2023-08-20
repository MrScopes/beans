package commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import utilities.miniMessage

class StopCommand : Command("stop") {
    init {
        defaultExecutor = CommandExecutor { _: CommandSender, _: CommandContext? ->
            Server.hub.players.forEach { it.kick("<red>The server has been stopped.".miniMessage())}
            Server.hub.saveChunksToStorage()
            MinecraftServer.stopCleanly()
        }
    }
}