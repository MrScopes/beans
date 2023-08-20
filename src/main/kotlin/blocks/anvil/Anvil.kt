package blocks.anvil

import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.inventory.type.AnvilInventory
import net.minestom.server.utils.NamespaceID

class Anvil : BlockHandler {
    init {
        ClientNameItem()
    }

    override fun onInteract(interaction: BlockHandler.Interaction): Boolean {
        val anvil = AnvilInventory("Anvil")
        interaction.player.openInventory(anvil)
        return true
    }
    override fun getNamespaceId(): NamespaceID {
        return NamespaceID.from("minecraft:anvil")
    }
}