package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import me.mrscopes.beans.events.listeners.ChatListener
import me.mrscopes.beans.events.listeners.JoinListener
import me.mrscopes.beans.events.listeners.QuitListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Events {
    val chatListener = ChatListener()
    val joinListener = JoinListener()
    val quitListener = QuitListener()

    init {
        registerEvents(listOf(
            chatListener,
            joinListener,
            quitListener,
        ))
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
