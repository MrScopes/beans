package me.mrscopes.beans.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import me.mrscopes.beans.Beans
import me.mrscopes.beans.commands.economy.SellCommand
import me.mrscopes.beans.commands.general.*
import me.mrscopes.beans.commands.staff.AdminChatCommand
import me.mrscopes.beans.commands.staff.BroadcastCommand
import me.mrscopes.beans.commands.staff.CustomEnchantCommand
import me.mrscopes.beans.commands.staff.StaffChatCommand

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
                BroadcastCommand(),
                DiscordCommand(),
                MineCommand(),
                CustomEnchantCommand(),
                SellCommand()
            )
        )
    }

    companion object {
        lateinit var commandManager: PaperCommandManager

        private fun registerCommands(commands: List<BaseCommand>) {
            commands.forEach { command -> registerCommand(command) }
        }

        private fun registerCommand(command: BaseCommand) {
            commandManager.registerCommand(command)
        }
    }
}
