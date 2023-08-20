package commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentEnum
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import utilities.miniMessage

class Gamemode : Command("gamemode") {
    init {
        val gamemodeArgument = ArgumentType.Enum("gamemode", GameMode::class.java).setFormat(ArgumentEnum.Format.LOWER_CASED)

        defaultExecutor = CommandExecutor { sender: CommandSender, context: CommandContext? -> sender.sendMessage("<red>/gamemode <adventure | creative | spectator | survival>".miniMessage()) }
        addSyntax({ sender: CommandSender, context: CommandContext ->
            val gamemode = context.get(gamemodeArgument)
            (sender as Player).gameMode = gamemode
        }, gamemodeArgument)
    }
}