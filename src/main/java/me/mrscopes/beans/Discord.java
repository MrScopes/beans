package me.mrscopes.beans;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class Discord extends ListenerAdapter {
    private final JDA client;
    public JDA getClient() { return client; }

    private final TextChannel serverChat;
    public TextChannel getServerChat() {
        return serverChat;
    }

    private final TextChannel staffChat;
    public TextChannel getStaffChat() {
        return staffChat;
    }

    private final TextChannel adminChat;
    public TextChannel getAdminChat() {
        return adminChat;
    }

    public Discord(Beans beans) throws LoginException, InterruptedException {
        JDA tempClient = JDABuilder.createDefault(beans.getConfig().getString("discord token")).addEventListeners(this).build();
        tempClient.awaitReady();
        client = tempClient;

        serverChat = client.getTextChannelById(Objects.requireNonNull(beans.getConfig().getString("server chat id")));
        staffChat = client.getTextChannelById(Objects.requireNonNull(beans.getConfig().getString("staff chat id")));
        adminChat = client.getTextChannelById(Objects.requireNonNull(beans.getConfig().getString("admin chat id")));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getChannelType() != ChannelType.TEXT) return;

        Message message = event.getMessage();
        if (message.getContentRaw().length() < 1) return;
        String content = message.getContentStripped();
        String finalContent = content.length() >= 200 ? content.substring(0, 200) : content;

        Component displayName = displayNameFromMember(message.getMember());
        MiniMessage miniMessage = MiniMessage.miniMessage();
        MessageChannel channel = message.getChannel();

        if (channel.equals(serverChat)) {
            Bukkit.broadcast(miniMessage.deserialize("<aqua>[Discord] <gray><name><white>: <message>", Placeholder.component("name", displayName), Placeholder.component("message", Component.text(finalContent))));
        } else if (channel.equals(staffChat)) {
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("beans.staffchat")).forEach(player -> player.sendMessage(miniMessage.deserialize("\n<rainbow>**<reset> <gold>[Staff Chat] <yellow><name><dark_gray>:<reset> <message>\n", Placeholder.component("name", displayName), Placeholder.component("message", Component.text(finalContent)))));
        } else if (channel.equals(adminChat)) {
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("beans.adminchat")).forEach(player -> player.sendMessage(miniMessage.deserialize("\n<rainbow>**<reset> <dark_red>[Admin Chat] <red><name><dark_gray>:<reset> <message>\n", Placeholder.component("name", displayName), Placeholder.component("message", Component.text(finalContent)))));
        }
    }

    public static Component displayNameFromMember(Member member) {
        Role highestRole = member.getRoles().get(0);
        if (highestRole != null)
            return MiniMessage.miniMessage().deserialize(String.format("<#%s>%s <white>%s", Integer.toHexString(highestRole.getColor().getRGB()).substring(2), highestRole.getName(), member.getUser().getAsTag()));
        else
            return Component.text(member.getUser().getAsTag());
    }
}
