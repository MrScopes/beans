package me.mrscopes.beans.enchantments.rare.pickaxe

import me.mrscopes.beans.enchantments.EnchantmentRarity
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.enchantments.EnchantmentType
import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.isInMine
import me.mrscopes.beans.utilities.mongoPlayer
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Money : Enchantment("Money", 5, intArrayOf(10, 20, 30), EnchantmentRarity.RARE, EnchantmentType.PICKAXE) {
    override fun activate(item: ItemStack, level: Int, player: Player?, block: Block?) {
        if (block!!.location.isInMine()) {
            player!!.mongoPlayer()!!.money += level * 100
        }
    }

    override fun description(level: Int): String {
        return "${this.chance[level - 1]}% chance to give $${level * 100} when you mine"
    }
}