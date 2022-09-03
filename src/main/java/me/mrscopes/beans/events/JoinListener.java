package me.mrscopes.beans.events;

import me.mrscopes.beans.Beans;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(MiniMessage.miniMessage().deserialize("<player> joined the server.", Placeholder.component("player", Component.text(event.getPlayer().getName()))));
        Beans.getDiscord().getServerChat().sendMessage(String.format("✅ **%s** joined the server.", event.getPlayer().getName())).queue();
    }
}
