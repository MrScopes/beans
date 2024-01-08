package me.mrscopes.beans.events

import me.mrscopes.beans.Beans
import me.mrscopes.beans.enchantments.listeners.EnchBlockBreakListener
import me.mrscopes.beans.events.listeners.*
import me.mrscopes.beans.regions.WorldGuard
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Events {
    val chatListener = ChatListener()

    init {
        registerListeners(
            listOf(

                /*
                Generic Listeners
                 */

                chatListener,
                JoinListener(),
                QuitListener(),
                RespawnListener(),
                InteractListener(),

                /*
                Other Listeners
                 */

                BowListeners(),
                WorldGuard(),

                /*
                Enchantment Listeners
                 */

                EnchBlockBreakListener()

            )
        )
    }

    companion object {
        fun registerListeners(listeners: List<Listener>) {
            listeners.forEach {
                Bukkit.getPluginManager().registerEvents(it, Beans.instance)
            }
        }
    }
}
