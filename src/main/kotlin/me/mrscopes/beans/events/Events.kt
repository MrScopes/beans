package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Events {
    val chatListener = ChatListener()
    val joinListener = JoinListener()
    val quitListener = QuitListener()


    init {
        registerEvents(chatListener)
        registerEvents(joinListener)
        registerEvents(quitListener)
    }

    companion object {
        fun registerEvents(listener: Listener?) {
            Bukkit.getPluginManager().registerEvents(listener!!, Beans.instance)
        }
    }
}
