package me.mrscopes.beans.utilities

import me.mrscopes.beans.Beans
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.enchantments.Enchantments
import me.mrscopes.beans.regions.WorldGuard
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun String.miniMessage() = MiniMessage.miniMessage().deserialize(this)
fun String.color() = Component.text(ChatColor.translateAlternateColorCodes('&', this))
fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)

fun Player.mongoPlayer() = Beans.mongo.mongoPlayers[this.uniqueId]
fun Player.sendColored(string: String) = this.sendMessage(string.color())
fun CommandSender.sendColored(string: String) = this.sendMessage(string.color())
fun OfflinePlayer.mongoPlayer() = Beans.mongo.mongoPlayers[this.uniqueId] ?: Beans.mongo.playerFromDatabase(this.uniqueId)

fun Location.isGuarded(): Boolean { return WorldGuard.isLocationGuarded(this) }
fun Location.isInMine(): Boolean { return WorldGuard.isInMine(this) }

fun ItemStack.addCustomEnchantment(enchantment: Enchantment, level: Int) = Enchantments.addEnchantment(this, enchantment, level)
fun ItemStack.customEnchantments() = Enchantments.getEnchantments(this)
