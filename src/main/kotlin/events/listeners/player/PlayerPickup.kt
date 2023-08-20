package events.listeners.player

import net.minestom.server.entity.Player
import net.minestom.server.event.item.PickupItemEvent


class PlayerPickup(event: PickupItemEvent) {
    init {
        val entity = event.livingEntity
        if (entity is Player) {
            val itemStack = event.itemEntity.itemStack
            event.isCancelled = !entity.inventory.addItemStack(itemStack)
        } else {
            event.isCancelled = true
        }
    }
}