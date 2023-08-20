package events.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.event.server.ServerListPingEvent

class ServerListPing(event: ServerListPingEvent) {
    init {
        val responseData = event.responseData

        responseData.description = Component.text("beans", TextColor.color(0x66b3ff))

        event.responseData = responseData
    }
}