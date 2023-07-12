package me.mrscopes.beans.commands.general

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import me.mrscopes.beans.Beans
import org.bukkit.entity.Player


@CommandAlias("spawn")
@Description("Teleport to spawn")
class SpawnCommand : BaseCommand() {
    @Default
    fun run(player: Player, target: Player?) {
        var teleport = player

        if (player.hasPermission("beans.mod")) {
            teleport = target ?: player
        }

        val spawnLocation = Beans.instance.config.getLocation("spawn")
        if (spawnLocation == null) {
            player.sendMessage("Spawn has not been set in the config.")
        } else {
            player.sendMessage("Teleporting to spawn.")
            teleport.teleport(spawnLocation!!)
        }
    }

}

@CommandAlias("setspawn")
@CommandPermission("beans.admin")
@Description("Set the spawn location")
class SetSpawnCommand : BaseCommand() {
    @Default
    fun run(player: Player) {
        Beans.instance.config.set("spawn", player.location)
        Beans.instance.saveConfig()
        player.sendMessage("Updated spawn location.")
    }
}