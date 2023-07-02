package me.mrscopes.beans.commands

import me.mrscopes.beans.Utilities
import me.mrscopes.beans.color
import me.mrscopes.beans.mongoPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BalanceCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = Utilities.playerFromArg(sender as Player, args.joinToString())
        val mongoPlayer = player?.mongoPlayer()

        if (mongoPlayer == null) {
            sender.sendMessage("&cThat player has never played before.".color())
            return true;
        }

        sender.sendMessage("${player.name} has ${mongoPlayer.money}")

        return true
    }
}
