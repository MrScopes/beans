package me.mrscopes.beans.mine;

import io.papermc.paper.command.brigadier.Commands;
import me.mrscopes.beans.Beans;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

import static me.mrscopes.beans.Utilities.hasPermission;

public class MineCommand {
    public MineCommand(Commands commands) {
        MineManager mineManager = Beans.getInstance().getMineManager();
        MiniMessage miniMessage = Beans.getInstance().getMiniMessage();

        commands.register(
                Commands.literal("mine")
                        .requires(hasPermission("beans.mod"))
                        .executes(context -> {
                            var now = System.nanoTime();
                            mineManager.restoreRandomMine();
                            long tookNs = System.nanoTime() - now;
                            double tookMs = tookNs / 1_000_000.0;
                            Bukkit.broadcast(
                                    miniMessage.deserialize("Reset mine in <took>ms.",
                                            Placeholder.parsed("took", String.format("%.2f", tookMs))
                                    ));

                            var now_tp = System.nanoTime();
                            mineManager.teleportPlayersToMineTop();
                            long tookTPNs = System.nanoTime() - now_tp;
                            double tookTPMs = tookTPNs / 1_000_000.0;
                            Bukkit.broadcast(
                                miniMessage.deserialize("Teleported players in <took>ms.",
                                        Placeholder.parsed("took", String.format("%.2f", tookTPMs))
                                ));
                            return 1;
                        })
                        .build()
        );
    }
}