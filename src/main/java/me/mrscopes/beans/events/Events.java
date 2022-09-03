package me.mrscopes.beans.events;

import me.mrscopes.beans.Beans;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Events {
    public Events () {
        registerEvents(new ChatListener());
        registerEvents(new JoinListener());
        registerEvents(new QuitListener());
    }

    public static void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, Beans.getInstance());
    }
}
