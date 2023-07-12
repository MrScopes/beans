package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import me.mrscopes.beans.events.listeners.ChatListener
import me.mrscopes.beans.events.listeners.JoinListener
import me.mrscopes.beans.events.listeners.QuitListener
import me.mrscopes.beans.events.listeners.RespawnListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Events {
    val chatListener = ChatListener()

    init {
        registerEvents(
            listOf(
                chatListener,
                JoinListener(),
                QuitListener(),
                RespawnListener()
            )
        )
    }

    companion object {
        fun registerEvents(listeners: List<Listener>) {
            listeners.forEach { listener -> registerEvent(listener) }
        }

        fun registerEvent(listener: Listener) {
            Bukkit.getPluginManager().registerEvents(listener, Beans.instance)
        }
    }
}
