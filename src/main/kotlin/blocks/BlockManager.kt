package blocks

import blocks.anvil.Anvil
import net.minestom.server.MinecraftServer

class BlockManager {
    init {
        MinecraftServer.getBlockManager().registerHandler("minecraft:enchanting_table", ::EnchantmentTable)
        MinecraftServer.getBlockManager().registerHandler("minecraft:anvil", ::Anvil)
    }
}