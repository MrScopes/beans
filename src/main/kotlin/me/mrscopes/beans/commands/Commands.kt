package me.mrscopes.beans.commands

import me.mrscopes.beans.economy.commands.BalanceCommand
import me.mrscopes.beans.commands.staff.StaffChat
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor

class Commands {
    init {
        registerCommand("adminchat", StaffChat())
        registerCommand("staffchat", StaffChat())
        registerCommand("balance", BalanceCommand())
    }

    companion object {
        fun registerCommand(name: String, command: CommandExecutor) {
            Bukkit.getPluginCommand(name)?.setExecutor(command)
        }
    }
}
