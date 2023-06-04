package me.mrscopes.beans

import me.mrscopes.beans.commands.Commands
import me.mrscopes.beans.events.Events
import org.bukkit.plugin.java.JavaPlugin

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