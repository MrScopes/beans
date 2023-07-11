package me.mrscopes.beans.economy.commands

import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.mongoPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PayCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size <= 1)
            return true

        val player = sender as Player
        val mongoPlayer = player.mongoPlayer()!!

        val target = Utilities.playerFromArg(player, args[0])
        val mongoTarget = target?.mongoPlayer()

        if (mongoTarget == null) {
            sender.sendMessage("&cThat player has never played before.".color())
            return true;
        }

        val amount =
            try {
                args[1].toDouble()
            } catch (e: Exception) {
                return false
            }

        if (amount <= 0)
            return false

        if (amount > mongoPlayer.money) {
            player.sendMessage("You don't have enough money.".color())
            return true
        }

        player.sendMessage("You paid ${target.name} $amount.")
        mongoPlayer.money -= amount
        mongoTarget.money =+ amount

        return true
    }
}
