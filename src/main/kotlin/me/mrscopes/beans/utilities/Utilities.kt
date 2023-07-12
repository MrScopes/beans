package me.mrscopes.beans.utilities

import me.mrscopes.beans.Beans
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
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

        return "$$moneyString"
    }

    fun whileLoop(condition: () -> Boolean, fn: () -> Unit, interval: Long): BukkitRunnable {
        val loopTask : BukkitRunnable = object : BukkitRunnable() {
            override fun run() {
                if (!condition()) {
                    cancel()
                    return
                }
                fn()
            }
        }
        loopTask.runTaskTimer(Beans.instance, 0, interval)
        return loopTask
    }
}