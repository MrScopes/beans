package me.mrscopes.beans.enchantments

import me.mrscopes.beans.enchantments.legendary.Repairing
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.romanNumeral
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

class Enchantments {
    private val enchantments: HashMap<String, Enchantment> = HashMap()

    init {
        listOf(

            /*
            General Enchantments
             */

            Repairing()

            /*
            Armor Enchantments
             */

            /*
            Bow Enchantments
             */

            /*
            Pickaxe Enchantments
             */

            /*
            Sword Enchantments
             */

        ).forEach {
            enchantments[it.name] = it
        }
    }

    fun tryToActivate(item: ItemStack, player: Player) {
        val enchantments = getEnchantmentsOf(item) ?: return

        enchantments.forEach {
            val chance = it.key.chance[it.value - 1]

            val random = Random.nextDouble(0.0, 100.0)

            if (random <= chance) {
                it.key.activate(item, it.value, player)
            }
        }
    }

    fun getEnchantmentsOf(item: ItemStack): HashMap<Enchantment, Int>? {
        val list = HashMap<Enchantment, Int>()

        val pdc = item.itemMeta.persistentDataContainer;
        val enchantments = pdc.get(NamespacedKey("beans", "enchantments"), PersistentDataType.TAG_CONTAINER) ?: return null

        enchantments.keys.forEach { ench ->
            val enchantment = this.enchantments[ench.value().replaceFirstChar { ench.value().first().uppercase() }]
            list[enchantment!!] = enchantments.get(NamespacedKey("beans", enchantment.name.lowercase()), PersistentDataType.INTEGER)!!
        }

        return list
    }

    fun addEnchantmentTo(item: ItemStack, enchantment: Enchantment, level: Int) {
        val meta = item.itemMeta;

        val pdc = meta.persistentDataContainer;

        var enchantmentsPDC = pdc.adapterContext.newPersistentDataContainer()

        if (getEnchantmentsOf(item) != null) {
            enchantmentsPDC = pdc.get(NamespacedKey("beans", "enchantments"), PersistentDataType.TAG_CONTAINER)!!
        }

        enchantmentsPDC.set(NamespacedKey("beans", enchantment.name.lowercase()), PersistentDataType.INTEGER, level)

        pdc.set(
            NamespacedKey("beans", "enchantments"),
            PersistentDataType.TAG_CONTAINER,
            enchantmentsPDC
        )

        item.itemMeta = meta;

        updateLore(item)
    }

    fun updateLore(item: ItemStack) {
        val enchantments = getEnchantmentsOf(item)

        item.editMeta { meta ->
            val lore = ArrayList<TextComponent>()

            enchantments!!.forEach {
                val color = when(it.key.rarity) {
                    EnchantmentRarity.COMMON -> "7"
                    EnchantmentRarity.RARE -> "b"
                    EnchantmentRarity.LEGENDARY -> "d"
                }
                lore.add("&$color${it.key.name} ${it.value.romanNumeral()} &8(&$color&o${it.key.description(it.value)}&8)".color())
            }

            meta.lore(lore)
        }
    }
}

enum class EnchantmentRarity {
    COMMON, RARE, LEGENDARY
}