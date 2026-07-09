package me.mrscopes;

import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.function.Predicate;

public class Utilities {
    public static Predicate<CommandSourceStack> hasPermission(String permission) {
        return source -> source.getSender().hasPermission(permission);
    }
}
