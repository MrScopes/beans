package me.mrscopes.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class Enchantments {
    public static NamespacedKey enchantmentNamespace = new NamespacedKey("beans", "enchantments");

    public static HashMap<String, Long> getEnchantments(ItemStack item) {
        HashMap<String, Long> enchantments = new HashMap<>();

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        PersistentDataContainer enchantmentContainer = pdc.get(enchantmentNamespace, PersistentDataType.TAG_CONTAINER);

        if (enchantmentContainer == null)
            return enchantments;

        for (NamespacedKey enchantment : enchantmentContainer.getKeys()) {
            Long level = enchantmentContainer.get(enchantment, PersistentDataType.LONG);
            enchantments.put(enchantment.toString(), level);
        }

        return enchantments;
    }

    public static void setEnchantmentLevel(ItemStack item, String enchantment, long level) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        PersistentDataContainer enchantmentContainer = pdc.getOrDefault(enchantmentNamespace, PersistentDataType.TAG_CONTAINER, pdc.getAdapterContext().newPersistentDataContainer());
        enchantmentContainer.set(new NamespacedKey(enchantmentNamespace.namespace(), enchantment), PersistentDataType.LONG, level);
        pdc.set(enchantmentNamespace, PersistentDataType.TAG_CONTAINER, enchantmentContainer);

        item.setItemMeta(meta);
    }
}
