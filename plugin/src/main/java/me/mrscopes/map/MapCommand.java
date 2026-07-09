package me.mrscopes.map;

import io.papermc.paper.command.brigadier.Commands;
import me.mrscopes.MrScopes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import static me.mrscopes.Utilities.hasPermission;

public class MapCommand {
    public MapCommand(Commands commands) {
        MiniMessage miniMessage = MrScopes.getInstance().getMiniMessage();
        MapManager mapManager = MrScopes.getInstance().getMapManager();

        Location corner1 = new Location(
                Bukkit.getWorld("world"),
                -102,
                -4,
                -102
        );

        Location corner2 = new Location(
                Bukkit.getWorld("world"),
                102,
                60,
                102
        );

        commands.register(
                Commands.literal("map")
                        .requires(hasPermission("beans.admin"))
                        .then(Commands.literal("save")
                                .executes(context -> {
                                        var now = System.nanoTime();
                                        mapManager.saveMap("beans", corner1, corner2);
                                        var took_ms = (System.nanoTime() - now) / 1_000_000;

                                        Bukkit.broadcast(
                                                miniMessage.deserialize("Saved map in <took>ms.", Placeholder.parsed("took", String.valueOf(took_ms))
                                                ));
                                        return 1;
                                }))
                        .then(Commands.literal("restore")
                                .executes(context -> {
                                        var now = System.nanoTime();
                                        mapManager.restoreMap("beans", corner1, corner2);
                                        var took_ms = (System.nanoTime() - now) / 1_000_000;
                                        Bukkit.broadcast(
                                                miniMessage.deserialize("Restored map in <took>ms.", Placeholder.parsed("took", String.valueOf(took_ms))
                                                ));
                                        return 1;
                                }))
                        .build()
        );
    }
}
