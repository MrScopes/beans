package me.mrscopes.beans.utilities

import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import java.text.NumberFormat


object Utilities {
    fun broadcastToStaff(message: TextComponent) {
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("beans.mod") || it.isOp) it.sendMessage(message)
        }
    }

    fun broadcastToAdmins(message: TextComponent) {
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("beans.admin") || it.isOp) it.sendMessage(message)
        }
    }

    fun moneyFormat(double: Double): String {
        val formatter = NumberFormat.getCurrencyInstance()
        var moneyString = formatter.format(double)

        if (moneyString.endsWith(".00")) {
            val centsIndex = moneyString.lastIndexOf(".00")
            if (centsIndex != -1) {
                moneyString = moneyString.substring(1, centsIndex)
            }
        }

        return moneyString
    }
}