package commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.utils.callback.CommandCallback
import utilities.miniMessage

class CommandManager {
    init {
        val commandManager = MinecraftServer.getCommandManager()

        commandManager.unknownCommandCallback = CommandCallback { sender: CommandSender, command: String? -> sender.sendMessage("<red>Unknown command \"/$command\".".miniMessage()) }

        commandManager.register(Gamemode())
        commandManager.register(Mine())
        commandManager.register(TpsCommand())
        commandManager.register(StopCommand())
        commandManager.register(SaveCommand())
    }
}