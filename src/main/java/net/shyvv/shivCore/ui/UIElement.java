package net.shyvv.shivCore.ui;

import net.shyvv.shivCore.ShivCoreMain;
import net.shyvv.shivCore.util.ItemStackBuilder;
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
        return ItemStackBuilder.setItemStackData(itemStack, "itemID", PersistentDataType.STRING, itemID);
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getID() {
        return itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), "itemID"), PersistentDataType.STRING);
    }

    public int getSlot() {
        return this.slot;
    }
}
