package me.mrscopes.beans

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

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

    fun playerFromArg(player: Player, arg: String) : OfflinePlayer? {
        return if (arg.isNotEmpty()) Bukkit.getOfflinePlayerIfCached(arg) else player
    }
}

fun String.color() = Component.text(ChatColor.translateAlternateColorCodes('&', this))
fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)
fun Player.mongoPlayer() = Beans.mongo.mongoPlayers[this.uniqueId]
fun OfflinePlayer.mongoPlayer() = Beans.mongo.mongoPlayers[this.uniqueId] ?: Beans.mongo.playerFromDatabase(this.uniqueId)