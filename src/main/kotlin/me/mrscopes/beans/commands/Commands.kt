package me.mrscopes.beans.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import me.mrscopes.beans.Beans
import me.mrscopes.beans.commands.general.KitCommand
import me.mrscopes.beans.commands.general.SetSpawnCommand
import me.mrscopes.beans.commands.general.SpawnCommand
import me.mrscopes.beans.commands.staff.AdminChatCommand
import me.mrscopes.beans.commands.staff.BroadcastCommand
import me.mrscopes.beans.commands.staff.StaffChatCommand
import me.mrscopes.beans.economy.commands.BalanceCommand
import me.mrscopes.beans.economy.commands.PayCommand

class Commands(plugin: Beans) {
    init {
        commandManager = PaperCommandManager(plugin)
        commandManager.enableUnstableAPI("brigadier");

        registerCommands(
            listOf(
                SpawnCommand(),
                SetSpawnCommand(),
                KitCommand(),
                StaffChatCommand(),
                AdminChatCommand(),
                BalanceCommand(),
                PayCommand(),
                BroadcastCommand()
            )
        )
    }

    companion object {
        lateinit var commandManager: PaperCommandManager

        fun registerCommands(commands: List<BaseCommand>) {
            commands.forEach { command -> registerCommand(command) }
        }

        fun registerCommand(command: BaseCommand) {
            commandManager.registerCommand(command)
        }
    }
}
