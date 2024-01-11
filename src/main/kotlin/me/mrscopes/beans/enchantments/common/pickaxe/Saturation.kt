package me.mrscopes.beans.enchantments.common.pickaxe

import me.mrscopes.beans.enchantments.EnchantmentRarity
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.enchantments.EnchantmentType
import me.mrscopes.beans.utilities.isInMine
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Saturation : Enchantment("Saturation", 2, intArrayOf(50, 100), EnchantmentRarity.COMMON, EnchantmentType.PICKAXE) {
    override fun activate(item: ItemStack, level: Int, player: Player?, block: Block?) {
        if (block!!.location.isInMine()) {
            player!!.foodLevel += 1
        }
    }

    override fun description(level: Int): String {
        if (level == 2) return "Feeds you when you mine"
        return "${this.chance[level - 1]}% chance to feed you when you mine"
    }
}