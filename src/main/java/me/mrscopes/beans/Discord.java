package me.mrscopes.beans;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.ServerOperator;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class Discord extends ListenerAdapter implements Listener {
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

        Bukkit.getPluginManager().registerEvents(this, beans);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        // delay so any changes to the message from the server is sent to discord
        Bukkit.getScheduler().runTaskLaterAsynchronously(Beans.getInstance(), () -> {
                Component message = event.message().replaceText(builder -> builder.match("@everyone").replacement("at everyone"));
                String paperSucksDamnWhyDoYouHaveToDoThisToGetMessageContentAsString = PlainTextComponentSerializer.plainText().serialize(message);
                serverChat.sendMessage(String.format("%s: %s", event.getPlayer().getName(), paperSucksDamnWhyDoYouHaveToDoThisToGetMessageContentAsString)).queue();
        }, 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        serverChat.sendMessage(String.format("✅ **%s** joined the server.", event.getPlayer().getName())).queue();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        serverChat.sendMessage(String.format("❌ **%s** left the server.", event.getPlayer().getName())).queue();
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
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("staff")).forEach(player -> player.sendMessage(miniMessage.deserialize("<green>[Staff] <white><name><white>: <message>", Placeholder.component("name", displayName), Placeholder.component("message", Component.text(finalContent)))));
        } else if (channel.equals(adminChat)) {
            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> player.sendMessage(miniMessage.deserialize("<red>[Admin] <white><name><white>: <message>", Placeholder.component("name", displayName), Placeholder.component("message", Component.text(finalContent)))));
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
