package me.mrscopes.beans.commands.economy

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.beans.utilities.Utilities
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.mongoPlayer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

@CommandAlias("sell")
@Description("Sell your inventory")
class SellCommand : BaseCommand() {
    @Default
    fun run(player: Player) {
        val mongoPlayer = player.mongoPlayer()!!
        var total = 0

        player.inventory.forEach {
            if (it != null) {
                val sellValue = sellValue(it.type)

                if (sellValue > 0) {
                    val money = sellValue * it.amount
                    total += money
                    mongoPlayer.money += money
                    player.inventory.remove(it)
                }
            }
        }

        player.sendMessage("&aYou sold your inventory for ${Utilities.moneyFormat(total)}.".color())
    }

    companion object {
        fun sellValue(material: Material?) : Int {
            return when(material) {
                Material.COBBLESTONE -> 1
                Material.COAL-> 2
                Material.RAW_IRON -> 3
                Material.DIAMOND -> 6
                else -> 0
            }
        }
    }
}