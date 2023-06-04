package me.mrscopes.beans

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object Utilities {
    fun broadcastToStaff(message: TextComponent) {
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("beans.staffchat") || it.isOp) it.sendMessage(message)
        }
    }

    fun broadcastToAdmins(message: TextComponent) {
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("beans.adminchat") || it.isOp) it.sendMessage(message)
        }
    }
}
fun String.color() = Component.text(ChatColor.translateAlternateColorCodes('&', this))
fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)