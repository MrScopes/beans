package me.mrscopes.beans.commands;

import me.mrscopes.beans.Beans;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StaffChat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }

        String message = String.join(" ", args);

        Beans.getDiscord().getStaffChat().sendMessage(String.format("**%s**: %s", sender.getName(), message)).queue();

        Component ingameMessage = MiniMessage.miniMessage().deserialize("\n<rainbow>**<reset> <gold>[Staff Chat] <reset><player><dark_gray>:<reset> <message>\n",
                Placeholder.component("player", Component.text(sender.getName())), Placeholder.component("message", Component.text(message)));

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("beans.command.staffchat")).forEach(player -> player.sendMessage(ingameMessage));

        return true;
    }
}
