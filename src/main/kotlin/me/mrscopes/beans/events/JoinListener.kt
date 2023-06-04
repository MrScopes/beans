package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[<green>+<dark_gray>]<reset> <gray>${event.player.name}", Placeholder.component("player", Component.text(event.player.name))))
        Beans.discord.serverChat.sendMessage("✅ **${event.player.name}** joined the server.").queue()
    }
}
