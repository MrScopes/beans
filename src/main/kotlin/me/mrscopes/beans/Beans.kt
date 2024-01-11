package me.mrscopes.beans

import me.mrscopes.beans.commands.Commands
import me.mrscopes.beans.enchantments.Enchantments
import me.mrscopes.beans.events.Events
import me.mrscopes.beans.mongo.Mongo
import org.bukkit.plugin.java.JavaPlugin

class Beans : JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        val tasks = listOf(
            StartupTask("Connect to mongo") { mongo = Mongo(this, config.getString("mongo url")!!) },
            StartupTask("Create commands") { commands = Commands(this) },
            StartupTask("Create events") { events = Events() }
        )

        tasks.forEach { task ->
            try {
                task.action()
                logger.info("Task '${task.description}' done successfully.")
            } catch (e: Exception) {
                e.printStackTrace()
                logger.severe("Couldn't ${task.description.lowercase()}:\n$e\nShutting down server.")
                server.shutdown()
            }
        }
    }

    override fun onDisable() {
        mongo.client.close()
        logger.info("Shutdown Mongo client.")
    }

    companion object {
        lateinit var instance: Beans
            private set
        lateinit var commands: Commands
            private set
        lateinit var events: Events
            private set
        lateinit var mongo: Mongo
            private set
        lateinit var enchantments: Enchantments
            private set
    }
}

data class StartupTask(val description: String, val action: () -> Unit)