package net.shyvv.shivCore.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InvUserInterface {
    String ID;
    Inventory inventory;
    List<UIElement> uiElements;
    ItemStack emptyFill;
    private InvUserInterface(String interfaceID, int invSize, String title, Builder builder) {
        this.ID = interfaceID;
        this.inventory = Bukkit.createInventory(null, invSize, Component.text(title));
        this.uiElements = builder.uiElements;
        this.emptyFill = builder.emptyFill;

        setContents();

        GUIManager.USER_INTERFACES.add(this);
    }

    public void openUI(Player p) {
        p.openInventory(inventory);
    }

    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        HumanEntity entity = e.getWhoClicked();
        if(clicked != null && entity instanceof Player p) {
            for (UIElement uie : uiElements) {
                if (clicked.equals(uie.getItemStack()) && uie instanceof UIRedirect uir) {
                    uir.onClick(p);
                    break;
                }
            }
        }
    }

    public void onInventoryInteract(InventoryInteractEvent e) {

    }

    public void onInventoryClose(InventoryCloseEvent e) {

    }

    public void setContents() {
        for(UIElement uie : uiElements) {
            inventory.setItem(uie.slot, uie.getItemStack());
        }
        for(int i = 0; i < inventory.getContents().length; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, emptyFill);
            }
        }
    }

    public String getID() {
        return this.ID;
    }

    public static class Builder {
        List<UIElement> uiElements = new ArrayList<>();
        ItemStack emptyFill = null;

        String interfaceID;
        int invSize;
        String title;

        public Builder(String interfaceID, int invSize, String title) {
            this.interfaceID = interfaceID;
            this.invSize = invSize;
            this.title = title;
        }

        public Builder addUIElement(UIElement uiElement) {
            uiElements.add(uiElement);
            return this;
        }

        public Builder addUIRedirect(UIRedirect uiRedirect) {
            uiElements.add(uiRedirect);
            return this;
        }

        public Builder fillEmpty(ItemStack itemStack) {
            emptyFill = itemStack;
            return this;
        }

        public InvUserInterface build() {
            return new InvUserInterface(interfaceID, invSize, title, this);
        }
    }
}
