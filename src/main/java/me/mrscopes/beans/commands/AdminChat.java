package me.mrscopes.beans.commands;

import me.mrscopes.beans.Beans;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminChat extends Command {
    AdminChat() {
        super("ac");
        this.setPermission("beans.adminchat");
        this.setDescription("Admin Chat between Discord and Minecraft.");
        this.setUsage(ChatColor.RED + "/ac <message>");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "/ac <message>");
            return true;
        }

        String message = String.join(" ", args);

        Beans.getDiscord().getAdminChat().sendMessage(String.format("**%s**: %s", sender.getName(), message)).queue();

        Component ingameMessage = MiniMessage.miniMessage().deserialize("\n<rainbow>**<reset> <dark_red>[Admin Chat] <red><player><dark_gray>:<reset> <message>\n",
                Placeholder.component("player", Component.text(sender.getName())), Placeholder.component("message", Component.text(message)));

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("beans.command.adminchat")).forEach(player -> player.sendMessage(ingameMessage));

        return true;
    }
}
