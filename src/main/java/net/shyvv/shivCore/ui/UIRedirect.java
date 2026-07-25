package net.shyvv.shivCore.ui;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UIRedirect extends UIElement implements UIClickable {
    String interfaceID;
    public UIRedirect(ItemStack itemStack, int slot, String itemID, String interfaceID) {
        super(itemStack, slot, itemID);
        this.interfaceID = interfaceID;
    }

    @Override
    public boolean clickConditions(InventoryClickEvent e) {
        return true;
    }

    @Override
    public void onClick(Player p) {
        p.playSound(p, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 1f, 1.75f);
        redirectInterface(p, interfaceID);
    }
}
