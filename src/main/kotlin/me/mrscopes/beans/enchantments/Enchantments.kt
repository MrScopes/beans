package me.mrscopes.beans.enchantments

import me.mrscopes.beans.enchantments.common.pickaxe.Saturation
import me.mrscopes.beans.enchantments.legendary.pickaxe.Blast
import me.mrscopes.beans.enchantments.legendary.Repairing
import me.mrscopes.beans.enchantments.legendary.pickaxe.Magnet
import me.mrscopes.beans.enchantments.rare.pickaxe.Money
import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.data
import me.mrscopes.beans.utilities.miniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

object Enchantments {
    val enchantments = listOf(
        Repairing(),

        Saturation(),
        Money(),
        Blast(),
        Magnet()
    )

    fun attempt(list: List<String>, item: ItemStack, player: Player? = null, block: Block? = null) {
        val enchantments = getEnchantments(item) ?: return
        enchantments.forEach {
            if (list.contains(it.key.name)) {
                if (chance(it.key.chance[it.value - 1])) {
                    it.key.activate(item, it.value, player, block)
                }
            }
        }
    }

    fun attempt(item: ItemStack, player: Player? = null, block: Block? = null) {
        val enchantments = getEnchantments(item) ?: return
        enchantments.forEach {
            if (chance(it.key.chance[it.value - 1])) {
                it.key.activate(item, it.value, player, block)
            }
        }
    }

    fun getEnchantments(item: ItemStack): HashMap<Enchantment, Int>? {
        val enchantments = item.data().get("enchantments", PersistentDataType.TAG_CONTAINER) ?: return null

        val list = HashMap<Enchantment, Int>()

        enchantments.keys.forEach { ench ->
            val enchantment = Enchantments.enchantments.first { it.name == ench.value().replaceFirstChar { ench.value().first().uppercase() } }
            list[enchantment] = enchantments.get(NamespacedKey("beans", enchantment.name.lowercase()), PersistentDataType.INTEGER)!!
        }

        return list
    }

    fun addEnchantment(item: ItemStack, enchantment: Enchantment, level: Int) {
        item.data().set("enchantments", enchantment.name.lowercase(), level)
        updateLore(item)
    }

    fun updateLore(item: ItemStack) {
        val enchantments = getEnchantments(item)!!

        item.editMeta { meta ->
            val lore = ArrayList<Component>()

            enchantments.filter { it.key.rarity == EnchantmentRarity.COMMON }.forEach {
                lore.add("&a${it.key.name} ${Utilities.romanNumeral(it.value)} &8(&7${it.key.description(it.value)}&8)".color())
            }

            enchantments.filter { it.key.rarity == EnchantmentRarity.RARE }.forEach {
                lore.add("&b${it.key.name} ${Utilities.romanNumeral(it.value)} &8(&7${it.key.description(it.value)}&8)".color())
            }

            enchantments.filter { it.key.rarity == EnchantmentRarity.LEGENDARY }.forEach {
                lore.add("<rainbow>${it.key.name} ${Utilities.romanNumeral(it.value)} <dark_gray>(<gray>${it.key.description(it.value)}<dark_gray>)".miniMessage().decoration(TextDecoration.ITALIC, false))
            }

            meta.lore(lore)
        }
    }

    fun chance(chance: Int): Boolean {
        val random = Random.nextDouble(0.0, 100.0)

        if (random <= chance) {
            return true
        }

        return false
    }
}

enum class EnchantmentRarity {
    COMMON, RARE, LEGENDARY
}

enum class EnchantmentType {
    ALL, ARMOR, BOW, PICKAXE, SWORD
}