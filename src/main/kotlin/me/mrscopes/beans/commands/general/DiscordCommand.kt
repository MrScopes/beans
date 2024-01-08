package me.mrscopes.beans.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Subcommand
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("discord")
@Description("beans discord")
class DiscordCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender) {
        val message = MiniMessage.miniMessage().deserialize("<click:open_url:'https://discord.gg/wjQz6ea6JV'>Click here to join our discord!")
        sender.sendMessage(message)
    }

    @Subcommand("link")
    fun link(player: Player, code: String) {
        player.sendMessage("Not yet implemented.")
    }

    @Subcommand("unlink")
    fun unlink(player: Player) {
        player.sendMessage("Not yet implemented.")
    }
}