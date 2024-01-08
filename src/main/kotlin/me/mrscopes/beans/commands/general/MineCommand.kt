package me.mrscopes.beans.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import org.bukkit.entity.Player
import me.mrscopes.beans.Beans
import me.mrscopes.beans.regions.Regions
import me.mrscopes.beans.utilities.color
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.Random

@CommandAlias("mine")
@Description("Reset the mine")
@CommandPermission("beans.mod")
class MineCommand : BaseCommand() {
    var random = Random()
    @Default
    fun run(player: Player) {
        Bukkit.broadcast("&a${player.name} is resetting the mine!".color())
        val now = System.currentTimeMillis()

        val world = Bukkit.getWorld("world")!!

        Regions.mineRegion.forEach { block ->
            run {
                val chance = random.nextInt(1000)

                var material = Material.STONE

                if (chance >= 950) material = Material.DIAMOND_ORE
                else if (chance >= 800) material = Material.IRON_ORE
                else if (chance >= 500) material = Material.COAL_ORE

                world.getBlockAt(block.x, block.y, block.z).type = material
            }
        }

        Bukkit.broadcast("&a${player.name} reset the mine in ${System.currentTimeMillis() - now}ms".color())
    }
}