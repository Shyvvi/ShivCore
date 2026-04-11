package net.shyvv.shivCore.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

public class UIElement {
    String itemID;
    ItemStack baseItem;
    int slot;

    // Only null ItemStack if you have unique logic for it such as a UIPlayerFeature
    public UIElement(@Nullable ItemStack itemStack, int slot, String itemID) {
        ItemStack nullStack = new ItemStackBuilder.Builder(Material.COMMAND_BLOCK)
                .setName(Component.text("NULL").color(NamedTextColor.RED))
                .build();
        if(itemStack == null) this.baseItem = designateItemStackID(nullStack);
        else this.baseItem = itemStack;
        this.slot = slot;
        this.itemID = itemID;
    }

    public ItemStack designateItemStackID(ItemStack itemStack) {
        return ItemStackBuilder.setItemStackData(itemStack, "itemID", PersistentDataType.STRING, itemID+"_button");
    }

    public ItemStack getItemStack(Player p) {
        if(this instanceof UIPlayerFeature uip) {
            return uip.provide(p);
        }
        return designateItemStackID(baseItem.clone());
    }

    public void redirectInterface(Player p, String interfaceID) {
        for(InvUserInterface iui : GUIManager.USER_INTERFACES) {
            if(iui.getID().equals(interfaceID)) {
                iui.openUI(p);
                break;
            }
        }
    }

    public String getID(Player p) {
        return getItemStack(p).getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING);
    }

    public int getSlot() {
        return this.slot;
    }
}
