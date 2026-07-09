package me.mrscopes.command;

import io.papermc.paper.command.brigadier.Commands;
import me.mrscopes.command.commands.TestCommand;
import me.mrscopes.deploy.SkriptDeployCommand;
import me.mrscopes.map.MapCommand;
import me.mrscopes.mine.MineCommand;

public class Registry {
    public Registry(Commands registrar) {
        new TestCommand(registrar);
        new MapCommand(registrar);
        new MineCommand(registrar);
        new SkriptDeployCommand(registrar);
    }
}