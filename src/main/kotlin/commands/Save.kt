package commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import utilities.miniMessage

class Save : Command("save") {
    init {
        defaultExecutor = CommandExecutor { sender: CommandSender, _: CommandContext? ->
            Server.instance.sendMessage("Server is being saved...".miniMessage())
            Server.instance.saveChunksToStorage()
            Server.instance.sendMessage("<green>Server has been saved.".miniMessage())
        }
    }
}