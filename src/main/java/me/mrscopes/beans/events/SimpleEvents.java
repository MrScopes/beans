package me.mrscopes.beans.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SimpleEvents implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        Component message = event.message();

        if (message.toString().contains("[item]")) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (!item.getType().equals(Material.AIR)) {
                // thanks paper discord
                Component name = item.displayName().hoverEvent(item.asHoverEvent());

                TextComponent component = Component.text()
                        .append(Component.text(item.getAmount() > 1 ? item.getAmount() + "x " : "", item.displayName().color()))
                        .append(name)
                        .build();

                message = message.replaceText(msg -> msg.once().match("\\[item\\]").replacement(component));
            }
        }

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<gray><player><white>: <message>",
                Placeholder.component("player", Component.text(event.getPlayer().getName())),
                Placeholder.component("message", message)));

        // for discord
        event.message(message);
    }
}
