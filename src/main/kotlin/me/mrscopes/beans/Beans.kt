package me.mrscopes.beans

import me.mrscopes.beans.commands.Commands
import me.mrscopes.beans.events.Events
import me.mrscopes.beans.mongo.Mongo
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.log

class Beans : JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        try {
            discord = Discord(this)
            logger.info("Connected to discord as ${discord.client.selfUser.asTag}")
        } catch (e: Exception) {
            logger.severe("Couldn't connect to discord: $e")
            server.shutdown()
        }
        try {
            mongo = Mongo(config.getString("mongo url")!!)
            logger.info("Connected to mongo successfully.")
        } catch (e: Exception) {
            logger.severe("Couldn't connect to mongo:\n$e\nShutting down server.")
            server.shutdown()
        }

        try {
            commands = Commands()
            logger.info("Registered commands successfully.")
        } catch (e: Exception) {
            logger.severe("Couldn't create commands:\n$e\nShutting down server.")
            server.shutdown()
        }
        try {
            events = Events()
            logger.info("Registered events successfully.")
        } catch (e: Exception) {
            logger.severe("Couldn't create events:\n$e\nShutting down server.")
            server.shutdown()
        }

        try {
            server.servicesManager.register(Economy::class.java, EconomyProvider(), this, ServicePriority.Highest)
            logger.info("Registered vault economy successfully.")
        } catch (e: Exception) {
            logger.severe("Couldn't register vault economy:\n$e\nShutting down server.")
            server.shutdown()
        }
    }

    override fun onDisable() {
        discord.client.shutdown()
        logger.info("Shutdown Discord client.")
    }

    companion object {
        lateinit var instance: Beans
            private set
        lateinit var discord: Discord
            private set
        lateinit var commands: Commands
            private set
        lateinit var events: Events
            private set
        lateinit var mongo: Mongo
            private set
    }
}