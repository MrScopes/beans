package me.mrscopes.beans.economy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Single
import me.mrscopes.beans.utilities.mongoPlayer
import me.mrscopes.beans.utilities.sendColored
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("pay")
@Description("Pay someone money")
class PayCommand : BaseCommand() {
    @Default
    @CommandCompletion("@players")
    fun run(sender: CommandSender, targetPlayer: OfflinePlayer, @Single amount: Double) {
        val player = sender as Player

        val mongoPlayer = player.mongoPlayer()!!
        val mongoTarget = targetPlayer.mongoPlayer()!!

        if (amount <= 0) {
            player.sendColored("&cYou must give a number larger than 0.")
            return
        }

        if (amount > mongoPlayer.money) {
            player.sendColored("&cYou don't have that much.")
            return
        }

        player.sendColored("&7You paid ${targetPlayer.name} $$amount.")
        targetPlayer.player?.sendColored("&7You were paid $$amount by ${player.name}.")

        mongoPlayer.money -= amount
        mongoTarget.money =+ amount
    }
}