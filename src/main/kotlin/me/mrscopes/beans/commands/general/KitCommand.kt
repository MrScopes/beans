package me.mrscopes.beans.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@CommandAlias("kit")
@Description("Get the starter kit")
class KitCommand : BaseCommand() {
    val kit = listOf(
        ItemStack(Material.IRON_SWORD),
        ItemStack(Material.IRON_PICKAXE),
        ItemStack(Material.ELYTRA)
    )

    @Default
    fun run(player: Player) {
        kit.forEach { player.inventory.addItem(it) }
    }
}