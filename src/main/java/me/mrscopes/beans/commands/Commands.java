package me.mrscopes.beans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;

public class Commands {
    public Commands () {
        registerCommand("adminchat", new AdminChat());
        registerCommand("staffchat", new StaffChat());
    }

    public static void registerCommand(String name, CommandExecutor command) {
        Bukkit.getPluginCommand(name).setExecutor(command);
    }
}
