package me.mrscopes.beans.utilities

import me.mrscopes.beans.Beans
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
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

    fun whileLoop(condition: () -> Boolean, fn: () -> Unit, interval: Long, delay: Long = 0): BukkitTask {
        val task = object : BukkitRunnable() {
            override fun run() {
                if (!condition()) {
                    cancel()
                    return
                }
                fn()
            }
        }.runTaskTimer(Beans.instance, delay, interval)

        return task
    }

    fun after(fn: () -> Unit, delay: Long) : BukkitTask {
        val task = object : BukkitRunnable() {
            override fun run() {
                fn()
            }
        }.runTaskLater(Beans.instance, delay)

        return task
    }

    fun async(fn: () -> Unit) : BukkitTask {
        val task = object : BukkitRunnable() {
            override fun run() {
                fn()
            }
        }.runTaskAsynchronously(Beans.instance)

        return task
    }

    fun getNearbyBlocks(location: Location, radius: Int): List<Block> {
        val blocks: MutableList<Block> = ArrayList<Block>()
        for (x in location.blockX - radius..location.blockX + radius) {
            for (y in location.blockY - radius..location.blockY + radius) {
                for (z in location.blockZ - radius..location.blockZ + radius) {
                    blocks.add(location.world.getBlockAt(x, y, z))
                }
            }
        }
        return blocks
    }
}