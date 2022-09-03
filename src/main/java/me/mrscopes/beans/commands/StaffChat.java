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

public class StaffChat extends Command {
    StaffChat() {
        super("sc");
        this.setPermission("beans.staffchat");
        this.setDescription("Staff Chat between Discord and Minecraft.");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "/sc <message>");
            return true;
        }

        String message = String.join(" ", args);

        Beans.getDiscord().getStaffChat().sendMessage(String.format("**%s**: %s", sender.getName(), message)).queue();

        Component ingameMessage = MiniMessage.miniMessage().deserialize("<rainbow>**<reset> <gold>[Staff Chat] <yellow><player><dark_gray>:<reset> <message>",
                Placeholder.component("player", Component.text(sender.getName())), Placeholder.component("message", Component.text(message)));

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("beans.command.staffchat")).forEach(player -> player.sendMessage(ingameMessage));

        return true;
    }
}
