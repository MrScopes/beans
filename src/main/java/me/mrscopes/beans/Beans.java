package me.mrscopes.beans;

import me.mrscopes.beans.commands.Commands;
import me.mrscopes.beans.events.Events;
import org.bukkit.plugin.java.JavaPlugin;

public final class Beans extends JavaPlugin {
    private static Beans instance;
    public static Beans getInstance() {
        return instance;
    }

    private static Discord discord;
    public static Discord getDiscord() {
        return discord;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("Enabled.");
        this.saveDefaultConfig();

        try {
            discord = new Discord(this);
            this.getLogger().info(String.format("Connected to discord as %s.", discord.getClient().getSelfUser().getAsTag()));
        } catch (Exception e) {
            this.getLogger().severe(String.format("Couldn't connect to discord: %s", e));
        }

        new Commands();
        new Events();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabled.");

        discord.getClient().shutdown();
        this.getLogger().info("Shutdown Discord client.");
    }
}
