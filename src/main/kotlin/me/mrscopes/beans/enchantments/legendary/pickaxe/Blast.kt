package me.mrscopes.beans.enchantments.legendary.pickaxe

import me.mrscopes.beans.enchantments.EnchantmentRarity
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.enchantments.EnchantmentType
import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.isInMine
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Blast : Enchantment("Blast", 3, intArrayOf(20, 40, 60), EnchantmentRarity.LEGENDARY, EnchantmentType.PICKAXE) {
    override fun activate(item: ItemStack, level: Int, player: Player?, block: Block?) {
        Utilities.getNearbyBlocks(block!!.location, level).forEach {
            if (it.location.isInMine()) {
                it.breakNaturally(player!!.inventory.itemInMainHand)
            }
        }
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% chance to break in $level block radius"
    }
}