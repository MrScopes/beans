package me.mrscopes.beans.enchantments.legendary

import me.mrscopes.beans.enchantments.EnchantmentRarity
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.utilities.Utilities
import org.bukkit.inventory.meta.Damageable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Repairing : Enchantment("Repairing", 3, intArrayOf(10, 20, 100), EnchantmentRarity.LEGENDARY) {
    override fun activate(item: ItemStack, level: Int, player: Player) {
        Utilities.after({
            item.editMeta {
                val damageable = it as Damageable
                damageable.damage -= level * 2
            }
        }, 1)
    }

    override fun description(level: Int): String {
        if (level == 3) return "Unbreakable Item"
        return "${this.chance[level - 1]}% to repair ${level * 2} durability on item use."
    }
}