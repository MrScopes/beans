package me.mrscopes.beans.commands;

import me.mrscopes.beans.Beans;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

public class Commands {
    public Commands () {
        registerCommand(new AdminChat());
        registerCommand(new StaffChat());
    }

    public static void registerCommand(Command command) {
        Bukkit.getCommandMap().register("beans", command);
    }
}
