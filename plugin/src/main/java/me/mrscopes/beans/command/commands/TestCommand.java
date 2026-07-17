package me.mrscopes.beans.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import me.mrscopes.beans.Beans;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.mrscopes.beans.Utilities.hasPermission;

public class TestCommand {
    public TestCommand(Commands commands) {
        MiniMessage miniMessage = Beans.getInstance().getMiniMessage();

        commands.register(
            Commands.literal("testcmd")
                    .requires(hasPermission("mrscopes.testcmd"))
                    .then(Commands.argument("target", StringArgumentType.word())
                            .requires(hasPermission("mrscopes.testcmd.target"))
                            .suggests((context, builder) -> {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    builder.suggest(player.getName());
                                }
                                return builder.buildFuture();
                            })
                            .then(Commands.literal("tree1")
                                    .executes(context -> {
                                        String name = StringArgumentType.getString(context, "target");
                                        Player target = Bukkit.getPlayerExact(name);

                                        if (target == null) {
                                            context.getSource().getSender().sendMessage(
                                                    miniMessage.deserialize(
                                                            "<red>Player not found."
                                                    )
                                            );
                                            return 0;
                                        }

                                        context.getSource().getSender().sendMessage(
                                                miniMessage.deserialize(
                                                        "Selected Player: <target>",
                                                        Placeholder.component("target", target.displayName())
                                                )
                                        );
                                        return 1;
                                    }))
                    )
                    .build()
        );
    }
}
