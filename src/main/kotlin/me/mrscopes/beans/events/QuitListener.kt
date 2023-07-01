package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[<red>-<dark_gray>]<reset> <gray>${event.player.name}", Placeholder.component("player", Component.text(event.player.name))))
        Beans.discord.serverChat.sendMessage("❌ **${event.player.name}** left the server.").queue()
        Beans.events.chatListener.antispam.remove(event.player.uniqueId)
    }
}