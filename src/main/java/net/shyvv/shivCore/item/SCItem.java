package net.shyvv.shivCore.item;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;

public class SCItem {
    ItemStack defaultItemStack;
    String itemID;
    private SCItem(ItemStack defaultItemStack, String itemID) {
        this.defaultItemStack = ItemStackBuilder.setItemStackData(defaultItemStack, "itemID", PersistentDataType.STRING, itemID);
        this.itemID = itemID;
        ItemManager.CUSTOM_ITEMS.add(this);
    }

    @ApiStatus.OverrideOnly
    public void onPlayerInteract(PlayerInteractEvent e) {}
    @ApiStatus.OverrideOnly
    public void onPlayerEntityInteract(PlayerInteractEntityEvent e) {}

    public ItemStack getDefaultItemStack() {
        return defaultItemStack;
    }

    public String getItemID() {
        return defaultItemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING);
    }

    public boolean matchesItemID(ItemStack is) {
        String itemStackID = is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING);
        return itemStackID != null && itemStackID.equals(itemID);
    }
}
