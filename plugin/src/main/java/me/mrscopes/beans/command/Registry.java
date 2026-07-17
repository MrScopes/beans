package me.mrscopes.beans.command;

import io.papermc.paper.command.brigadier.Commands;
import me.mrscopes.beans.command.commands.TestCommand;
import me.mrscopes.beans.map.MapCommand;
import me.mrscopes.beans.mine.MineCommand;

public class Registry {
    public Registry(Commands registrar) {
        new TestCommand(registrar);
        new MapCommand(registrar);
        new MineCommand(registrar);
    }
}