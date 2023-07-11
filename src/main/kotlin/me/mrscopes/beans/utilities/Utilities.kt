package me.mrscopes.beans.utilities

import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
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