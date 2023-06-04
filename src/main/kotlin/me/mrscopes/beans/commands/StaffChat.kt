package me.mrscopes.beans.commands

import me.mrscopes.beans.Beans
import me.mrscopes.beans.Utilities.broadcastToAdmins
import me.mrscopes.beans.Utilities.broadcastToStaff
import me.mrscopes.beans.color
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StaffChat : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return false
        }
        val message = args.joinToString()
        val discord = Beans.discord

        if (command.name == "staffchat") {
            broadcastToStaff("&4&l! &6[Staff Chat] &e${sender.name}&8: &f$message".color())
            discord.staffChat.sendMessage("${sender.name}: $message").queue()
        } else {
            broadcastToAdmins("&4&l! &4[Admin Chat] &c${sender.name}&8: &f$message".color())
            discord.adminChat.sendMessage("${sender.name}: $message").queue()
        }

        return true
    }
}
