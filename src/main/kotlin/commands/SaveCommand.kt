package commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import utilities.miniMessage

class SaveCommand : Command("save") {
    init {
        defaultExecutor = CommandExecutor { sender: CommandSender, _: CommandContext? ->
            Server.hub.sendMessage("Server is being saved...".miniMessage())
            Server.hub.saveChunksToStorage()
            Server.hub.sendMessage("<green>Server has been saved.".miniMessage())
        }
    }
}