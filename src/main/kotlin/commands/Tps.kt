package commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import utilities.miniMessage

class Tps : Command("tps") {
    init {
        defaultExecutor = CommandExecutor { sender: CommandSender, _: CommandContext? ->
            val tps = MinecraftServer.TICK_PER_SECOND

            val tpsColor = when {
                tps < 15 -> "light_red"
                tps < 19 -> "yellow"
                else -> "green"
            }

            sender.sendMessage("TPS: <$tpsColor>${MinecraftServer.TICK_PER_SECOND}".miniMessage())
            sender.sendMessage("Tick MS: ${MinecraftServer.TICK_MS}ms")
        }
    }
}