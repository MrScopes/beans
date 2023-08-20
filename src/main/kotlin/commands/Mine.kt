package commands

import Server
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.block.Block
import java.util.*

class Mine : Command("mine") {
    init {
        for (x in -19..19) {
            for (y in 11..37) {
                for (z in -19..19) {
                    positions.add(Pos(x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        defaultExecutor = CommandExecutor { sender: CommandSender, _: CommandContext? ->
            val now = System.currentTimeMillis()
            resetMine()
            sender.sendMessage("Reset ${positions.size} blocks in ${System.currentTimeMillis() - now}ms.")
        }
    }

    fun resetMine() {
        positions.forEach {
            val chance = rand.nextInt(1000)

            var block = Block.STONE

            if (chance >= 950) block = Block.DIAMOND_ORE
            else if (chance >= 800) block = Block.IRON_ORE
            else if (chance >= 500) block = Block.COAL_ORE

            Server.hub.setBlock(it, block)
        }
    }

    companion object {
        var positions = ArrayList<Pos>()
        val rand = Random()
    }
}