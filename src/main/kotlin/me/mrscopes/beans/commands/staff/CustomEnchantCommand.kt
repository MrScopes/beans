package me.mrscopes.beans.commands.staff

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.beans.Beans
import me.mrscopes.beans.enchantments.Enchantment
import me.mrscopes.beans.enchantments.Enchantments
import me.mrscopes.beans.utilities.addCustomEnchantment
import org.bukkit.entity.Player

@CommandAlias("ce")
@CommandPermission("beans.admin")
@Description("Custom Enchant Test")
class CustomEnchantCommand: BaseCommand() {
    @Default
    fun run(player: Player, enchantment: String, level: Int) {
        player.inventory.itemInMainHand.addCustomEnchantment(Enchantments.enchantments.first { it.name == enchantment }, level)
    }
}