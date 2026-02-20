package net.shyvv.shivCore.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UIRedirect extends UIElement {
    String interfaceID;
    public UIRedirect(ItemStack itemStack, int slot, String itemID, String interfaceID) {
        super(itemStack, slot, itemID);
        this.interfaceID = interfaceID;
    }

    public void onClick(Player p) {
        for(InvUserInterface iui : GUIManager.USER_INTERFACES) {
            if(iui.getID().equals(interfaceID)) {
                iui.openUI(p);
                break;
            }
        }
    }
}
