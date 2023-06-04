package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Events {
    init {
        registerEvents(ChatListener())
        registerEvents(JoinListener())
        registerEvents(QuitListener())
    }

    companion object {
        fun registerEvents(listener: Listener?) {
            Bukkit.getPluginManager().registerEvents(listener!!, Beans.instance)
        }
    }
}
