package events.listeners.player

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.ItemEntity
import net.minestom.server.event.item.ItemDropEvent
import java.time.Duration

class PlayerDrop(event: ItemDropEvent) {
    init {
        val player = event.player
        val droppedItem = event.itemStack

        val playerPos = player.position
        val itemEntity = ItemEntity(droppedItem)
        itemEntity.setPickupDelay(Duration.ofMillis(500))
        itemEntity.setInstance(player.instance, playerPos.withY { y -> y + 1.5 })
        val velocity: Vec = playerPos.direction().mul(6.0)
        itemEntity.velocity = velocity
    }
}