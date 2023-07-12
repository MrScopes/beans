package me.mrscopes.beans.economy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.mongoPlayer
import me.mrscopes.beans.utilities.sendColored
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("balance|bal")
@Description("Pocket watch")
class BalanceCommand : BaseCommand() {
    @Default
    @CommandCompletion("@players")
    fun run(sender: CommandSender, targetPlayer: Player?) {
        val target = targetPlayer ?: sender as Player
        val mongoPlayer = target.mongoPlayer()

        if (mongoPlayer == null) {
            sender.sendMessage("&cThat player has never played before.".color())
            return
        }

        (sender as Player).sendColored("&7${target.name} has $${mongoPlayer.money}.")
    }
}
