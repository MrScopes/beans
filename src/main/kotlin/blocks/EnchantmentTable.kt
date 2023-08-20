package blocks

import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.inventory.type.EnchantmentTableInventory
import net.minestom.server.utils.NamespaceID
import org.jetbrains.annotations.NotNull


class EnchantmentTable : BlockHandler {
    override fun onInteract(interaction: BlockHandler.Interaction): Boolean {
        val table = EnchantmentTableInventory("Enchantment Table")
        interaction.player.openInventory(table)
        return true
    }

    @NotNull
    override fun getNamespaceId(): NamespaceID {
        return NamespaceID.from("minecraft:enchanting_table")
    }
}