package net.shyvv.shivCore.item;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;

public abstract class SCItem {
    ItemStack defaultItemStack;
    String itemID;
    public SCItem(ItemStack defaultItemStack, String itemID) {
        this.defaultItemStack = ItemStackBuilder.setItemStackData(defaultItemStack, "itemID", PersistentDataType.STRING, itemID);
        this.itemID = itemID;
        ItemManager.CUSTOM_ITEMS.add(this);
    }

    public abstract void onPlayerInteract(PlayerInteractEvent e);
    public abstract void onPlayerEntityInteract(PlayerInteractEntityEvent e);

    public ItemStack getDefaultItemStack() {
        return defaultItemStack;
    }

    public String getItemID() {
        ItemMeta im = defaultItemStack.getItemMeta();
        return im != null ? im.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING) : "";
    }

    public boolean matchesItemID(ItemStack is) {
        String itemStackID = is.getItemMeta() != null ? is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING) : null;
        return itemStackID != null && itemStackID.equals(itemID);
    }
}
