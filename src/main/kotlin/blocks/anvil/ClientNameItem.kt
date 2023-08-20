package blocks.anvil

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.inventory.type.AnvilInventory
import net.minestom.server.network.packet.client.play.ClientNameItemPacket
import utilities.miniMessage

class ClientNameItem {
    init {
        MinecraftServer.getPacketListenerManager().setListener(ClientNameItemPacket::class.java) { packet: ClientNameItemPacket, player: Player ->
            val inventory = player.openInventory as AnvilInventory
            inventory.setItemStack(2, inventory.getItemStack(0).withDisplayName(packet.itemName.miniMessage()))
        }
    }
}