package me.mrscopes.beans.commands.staff

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.beans.levels.Levels
import me.mrscopes.beans.utilities.color
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

@CommandAlias("broadcast|bc")
@CommandPermission("beans.mod")
@Description("Broadcast a message to the server")
class BroadcastCommand : BaseCommand() {
    @Default
    fun run(sender: CommandSender, args: Array<String>) {
        val message = args.joinToString(" ")
        Bukkit.broadcast("\n&6beans: &e$message\n".color())

        for (i in 1..100) Bukkit.broadcast(Levels.levelDisplay(i))
    }
}