package net.shyvv.shivCore.item;

import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.ShivCore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemStackBuilder {
    public static <P, C>  ItemStack setItemStackData(ItemStack item, String key, PersistentDataType<P, C> type, C value) {
        ItemMeta im = item.getItemMeta();
        item.setItemMeta(setItemMetaData(im, key, type, value));
        return item;
    }

    public static <P, C>  ItemMeta setItemMetaData(ItemMeta meta, String key, PersistentDataType<P, C> type, C value) {
        meta.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), key), type, value);
        return meta;
    }

    public static class Builder {
        ItemStack baseItem;
        ItemMeta itemMeta;
        public Builder(Material material) {
            this.baseItem = new ItemStack(material);
            this.itemMeta = baseItem.getItemMeta();
        }

        public Builder setMaxStackSize(int value) {
            this.itemMeta.setMaxStackSize(value);
            return this;
        }

        public Builder setCustomModelData(int value) {
            this.itemMeta.setCustomModelData(value);
            return this;
        }

        public Builder setName(Component name) {
            this.itemMeta.itemName(name);
            return this;
        }

        public Builder setLore(List<Component> lore) {
            this.itemMeta.lore(lore);
            return this;
        }

        public Builder setHiddenTooltip(boolean value) {
            this.itemMeta.setHideTooltip(value);
            return this;
        }

        public <P, C>  Builder setData(String key, PersistentDataType<P, C> type, C value) {
            ItemStackBuilder.setItemMetaData(itemMeta, key, type, value);
            return this;
        }

        public ItemStack build() {
            baseItem.setItemMeta(itemMeta);
            return baseItem;
        }
    }
}
