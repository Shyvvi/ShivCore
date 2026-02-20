package net.shyvv.shivCore.ui;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.item.ItemStackBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class UIElement {
    ItemStack itemStack;
    int slot;
    public UIElement(ItemStack itemStack, int slot, String itemID) {
        this.itemStack = designateItemStackID(itemStack, itemID);
        this.slot = slot;
    }

    private ItemStack designateItemStackID(ItemStack itemStack, String itemID) {
        return ItemStackBuilder.setItemStackData(itemStack, "itemID", PersistentDataType.STRING, itemID+"_button");
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getID() {
        return itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING);
    }

    public int getSlot() {
        return this.slot;
    }
}
