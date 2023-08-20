package commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import utilities.miniMessage

class Stop : Command("stop") {
    init {
        defaultExecutor = CommandExecutor { _: CommandSender, _: CommandContext? ->
            Server.instance.players.forEach { it.kick("<red>The server has been stopped.".miniMessage())}
            Server.instance.saveChunksToStorage()
            MinecraftServer.stopCleanly()
        }
    }
}