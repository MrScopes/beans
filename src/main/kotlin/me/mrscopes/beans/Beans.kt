package me.mrscopes.beans

import me.mrscopes.beans.commands.Commands
import me.mrscopes.beans.events.Events
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil

class Beans : JavaPlugin() {
    override fun onEnable() {
        instance = this
        logger.info("Enabled.")
        saveDefaultConfig()
        try {
            discord = Discord(this)
            logger.info(String.format("Connected to discord as %s.", discord.client.selfUser.asTag))
        } catch (e: Exception) {
            logger.severe(String.format("Couldn't connect to discord: %s", e))
        }
        Commands()
        Events()
    }

    override fun onDisable() {
        logger.info("Disabled.")
        discord.client.shutdown()
        logger.info("Shutdown Discord client.")
    }

    companion object {
        lateinit var instance: Beans
            private set
        lateinit var discord: Discord
            private set
    }
}

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

fun String.color() = Component.text(ChatColor.translateAlternateColorCodes('&', this))
fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)