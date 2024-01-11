package me.mrscopes.beans.enchantments.legendary.pickaxe

import me.mrscopes.beans.enchantments.EnchantmentRarity
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.enchantments.EnchantmentType
import me.mrscopes.beans.utilities.Utilities
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicLong

class Magnet : Enchantment("Magnet", 3, intArrayOf(30, 50, 100), EnchantmentRarity.LEGENDARY, EnchantmentType.PICKAXE) {
    override fun activate(item: ItemStack, level: Int, player: Player?, block: Block?) {
        Utilities.after({
            val x = AtomicLong(30)
            Utilities.whileLoop({ x.get() > 0 }, {
                block!!.location.getNearbyEntities(4.0, 4.0, 4.0).forEach {
                    if (it.type == EntityType.DROPPED_ITEM) {
                        val direction = player!!.location.toVector().subtract(it.location.toVector()).normalize()
                        it.velocity = direction.multiply(0.8)
                    }
                }

                x.set(x.get() - 1)
            }, 1);
        }, 10)
    }

    override fun description(level: Int): String {
        if (level == 3) return "Pulls in all items in 4 radius when you mine"
        return "${this.chance[level - 1]}% to pull in all items in 4 radius when you mine"
    }
}