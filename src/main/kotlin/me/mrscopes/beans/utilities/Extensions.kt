package me.mrscopes.beans.utilities

import me.mrscopes.beans.Beans
import me.mrscopes.beans.enchantments.Enchantment
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

fun String.color() = Component.text(ChatColor.translateAlternateColorCodes('&', this))
fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)
fun Player.mongoPlayer() = Beans.mongo.mongoPlayers[this.uniqueId]
fun Player.sendColored(string: String) = this.sendMessage(string.color())
fun CommandSender.sendColored(string: String) = this.sendMessage(string.color())
fun OfflinePlayer.mongoPlayer() = Beans.mongo.mongoPlayers[this.uniqueId] ?: Beans.mongo.playerFromDatabase(this.uniqueId)

fun ItemStack.addCustomEnchantment(enchantment: Enchantment, level: Int) = Beans.enchantments.addEnchantmentTo(this, enchantment, level)

fun Int.romanNumeral() = run {
    when(this) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        else -> this
    }
}