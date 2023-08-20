package events

import events.listeners.*
import events.listeners.player.*

import net.minestom.server.MinecraftServer
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.*
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.server.ServerListPingEvent

class EventManager {
    init {
        val handler = MinecraftServer.getGlobalEventHandler()

        handler.addListener(ServerListPingEvent::class.java, ::ServerListPing)
        handler.addListener(PlayerLoginEvent::class.java, ::PlayerLogin)
        handler.addListener(PlayerSpawnEvent::class.java, ::PlayerSpawn)
        handler.addListener(PlayerCommandEvent::class.java, ::PlayerCommand)
        handler.addListener(PlayerChatEvent::class.java, ::PlayerChat)
        handler.addListener(ItemDropEvent::class.java, ::PlayerDrop)
        handler.addListener(PickupItemEvent::class.java, ::PlayerPickup)
    }
}