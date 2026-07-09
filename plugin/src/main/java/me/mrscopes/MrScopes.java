package me.mrscopes;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.mrscopes.command.Registry;
import me.mrscopes.map.MapManager;
import me.mrscopes.mine.FastBlocks;
import me.mrscopes.mine.MineManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class MrScopes extends JavaPlugin {
    private static MrScopes instance;

    public static MrScopes getInstance() {
        return instance;
    }

    private MiniMessage miniMessage;

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    private MapManager mapManager;

    public MapManager getMapManager() {
        return mapManager;
    }

    private MineManager mineManager;

    public MineManager getMineManager() {
        return mineManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        miniMessage = MiniMessage.miniMessage();

        mapManager = new MapManager(this);
        mineManager = new MineManager(this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            new Registry(commands.registrar());
        });

        FastBlocks.warmupRadius(20);

        Bukkit.getScheduler().runTask(this, () -> {
            mineManager.generateMineSnapshots(() -> {
                getLogger().info("Mines loaded: " + mineManager.getMineCount());
                mineManager.restoreRandomMine();
                getLogger().info("Restored a random mine.");
            });
        });

        // Optional warmup for FastBlocks JIT/cache. This runs in a harmless location only if world exists.
        // Remove this if you do not want startup world edits.
        World world = Bukkit.getWorld("world");
        if (world != null) {
            Location warmupLocation = new Location(world, 0, 15, 0);
            for (int i = 0; i < 10; i++) {
                FastBlocks.nuke(warmupLocation, 20);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mineManager != null) {
            mineManager.stopBeaconSoundTask();
        }
    }
}
