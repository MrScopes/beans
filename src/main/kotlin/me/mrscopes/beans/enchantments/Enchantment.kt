package me.mrscopes.beans.enchantments

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class Enchantment(var name: String, var maxLevel: Int, var chance: IntArray, var rarity: EnchantmentRarity, var type: EnchantmentType) {
    open fun activate(item: ItemStack, level: Int, player: Player?, block: Block?) {
        return;
    }

    open fun description(level: Int): String {
        return "$name enchantment"
    }
}