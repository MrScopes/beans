package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Events {
    val chatListener = ChatListener()
    val joinListener = JoinListener()
    val quitListener = QuitListener()
    val connectListener = ConnectListener()

    init {
        registerEvents(listOf(
            chatListener,
            joinListener,
            quitListener,
            connectListener
        ))
    }

    companion object {
        fun registerEvents(listeners: List<Listener>) {
            listeners.forEach { listener -> Bukkit.getPluginManager().registerEvents(listener, Beans.instance) }
        }
    }
}
