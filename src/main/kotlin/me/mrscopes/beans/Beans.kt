package me.mrscopes.beans

import me.mrscopes.beans.commands.Commands
import me.mrscopes.beans.discord.Discord
import me.mrscopes.beans.economy.EconomyProvider
import me.mrscopes.beans.events.Events
import me.mrscopes.beans.mongo.Mongo
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class Beans : JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        data class StartupTask(val description: String, val action: () -> Unit)

        val tasks = listOf(
            StartupTask("Connect to discord") { discord = Discord(this) },
            StartupTask("Connect to mongo") { mongo = Mongo(this, config.getString("mongo url")!!) },
            StartupTask("Create commands") { commands = Commands(this) },
            StartupTask("Create events") { events = Events() },
            StartupTask("Register vault economy") {
                server.servicesManager.register(
                    Economy::class.java,
                    EconomyProvider(),
                    this,
                    ServicePriority.Highest
                )
            }
        )

        tasks.forEach { task ->
            try {
                task.action()
                logger.info("${task.description} successfully.")
            } catch (e: Exception) {
                logger.severe("Couldn't ${task.description.toLowerCase()}:\n$e\nShutting down server.")
                server.shutdown()
            }
        }
    }

    override fun onDisable() {
        discord.client.shutdown()
        logger.info("Shutdown Discord client.")

        mongo.client.close()
        logger.info("Shutdown Mongo client.")
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