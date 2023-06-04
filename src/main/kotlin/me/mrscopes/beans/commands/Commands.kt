package me.mrscopes.beans.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor

class Commands {
    init {
        registerCommand("adminchat", StaffChat())
        registerCommand("staffchat", StaffChat())
    }

    companion object {
        fun registerCommand(name: String, command: CommandExecutor) {
            Bukkit.getPluginCommand(name)?.setExecutor(command)
        }
    }
}
