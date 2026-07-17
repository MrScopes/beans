package me.mrscopes.enchantments;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CustomEnchantments {
    public static NamespacedKey enchantmentNamespace = new NamespacedKey("beans", "enchantments");

    public static ArrayList<Enchantment> getEnchantments(ItemStack item) {
        ArrayList<Enchantment> enchantments = new ArrayList<>();

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        PersistentDataContainer enchantmentContainer = pdc.get(enchantmentNamespace, PersistentDataType.TAG_CONTAINER);

        if (enchantmentContainer == null)
            return enchantments;

        for (NamespacedKey enchantment : enchantmentContainer.getKeys()) {
            Long level = enchantmentContainer.get(enchantment, PersistentDataType.LONG);
            enchantments.add(new Enchantment(enchantment.getKey(), level));
        }

        return enchantments;
    }

    public static void setEnchantmentLevel(ItemStack item, String enchantment, Long level) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        PersistentDataContainer enchantmentContainer = pdc.getOrDefault(enchantmentNamespace, PersistentDataType.TAG_CONTAINER, pdc.getAdapterContext().newPersistentDataContainer());
        if (level == 0) {
            enchantmentContainer.remove(new NamespacedKey(enchantmentNamespace.namespace(), enchantment));
        } else {
            enchantmentContainer.set(new NamespacedKey(enchantmentNamespace.namespace(), enchantment), PersistentDataType.LONG, level);
        }
        pdc.set(enchantmentNamespace, PersistentDataType.TAG_CONTAINER, enchantmentContainer);

        item.setItemMeta(meta);
    }

    public static @Nullable Long getEnchantmentLevel(ItemStack item, String enchantment) {
        PersistentDataContainerView pdc = item.getPersistentDataContainer();
        PersistentDataContainer enchantmentContainer = pdc.get(enchantmentNamespace, PersistentDataType.TAG_CONTAINER);
        if (enchantmentContainer == null)
            return null;

        return enchantmentContainer.get(new NamespacedKey(enchantmentNamespace.namespace(), enchantment), PersistentDataType.LONG);
    }
    
    public record Enchantment(String enchantment, Long level) {}
}
