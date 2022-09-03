package me.mrscopes.beans.events;

import me.mrscopes.beans.Beans;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(MiniMessage.miniMessage().deserialize("<player> left the server.", Placeholder.component("player", Component.text(event.getPlayer().getName()))));
        Beans.getDiscord().getServerChat().sendMessage(String.format("✅ **%s** joined the server.", event.getPlayer().getName())).queue();
    }
}
