package me.mrscopes.beans.oldcommands.staff

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.beans.Beans
import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.color
import org.bukkit.command.CommandSender

@CommandAlias("staffchat|sc")
@CommandPermission("beans.mod")
@Description("Chat between all staff members")
class StaffChatCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, args: Array<String>) {
        val message = args.joinToString()
        Utilities.broadcastToStaff("&4&l! &6[Staff Chat] &e${sender.name}&8: &f$message".color())
        Beans.discord.staffChat.sendMessage("${sender.name}: $message").queue()
    }
}

@CommandAlias("adminchat|ac")
@CommandPermission("beans.admin")
@Description("Chat between all admins")
class AdminChatCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, args: Array<String>) {
        val message = args.joinToString()
        Utilities.broadcastToAdmins("&4&l! &4[Admin Chat] &c${sender.name}&8: &f$message".color())
        Beans.discord.adminChat.sendMessage("${sender.name}: $message").queue()
    }
}