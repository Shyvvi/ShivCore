package net.shyvv.shivCore.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.List;

public class InvUserInterface {
    String ID;
    boolean cancelsInteraction;
    int unicode;
    int invSize;
    TextComponent title;
    Map<UUID, Inventory> inventories = new HashMap<>();
    List<UIElement> baseUIElements;
    ItemStack emptyFill;
    private InvUserInterface( Builder builder) {
        this.ID = builder.interfaceID;
        this.invSize = builder.invSize;
        this.title = Component.text().append(Component.text(builder.negativeSpacesGui + Character.toString(unicode), NamedTextColor.WHITE)).append(Component.text(builder.negativeSpacesTitle + title, builder.color)).build();
        ShivCore.log.info("Interface {} with unicode value of: {}", builder.interfaceID, Character.toString(unicode));
        this.baseUIElements = builder.uiElements;
        this.emptyFill = builder.emptyFill;
        this.unicode = builder.unicode;
        this.cancelsInteraction = builder.cancelsInteraction;
        GUIManager.USER_INTERFACES.add(this);
    }

    public void openUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, invSize, title);
        inventories.put(p.getUniqueId(), inv);
        setContents(p, inv);
        p.openInventory(inv);
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if(!cancelsInteraction) {
            e.setCancelled(true);
        }
        ItemStack clicked = e.getCurrentItem();
        HumanEntity entity = e.getWhoClicked();
        if(clicked != null && entity instanceof Player p && e.getInventory().equals(getInvGUI(p))) {
            for (UIElement uie : baseUIElements) {
                if (getItemID(clicked).equals(uie.getID(p))) {
                    if(uie instanceof UIClickable uir) {
                        uir.onClick(p);
                    } else if(uie instanceof UICancelsInteraction) {
                        e.setCancelled(true);
                    }
                    break;
                }
            }
        }
    }

    private String getItemID(ItemStack is) {
        if(is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), "itemID"))) {
            return is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING);
        }
        return "";
    }

    public void onInventoryInteract(InventoryInteractEvent e) {}
    public void onInventoryOpen(InventoryOpenEvent e) {}
    public void onInventoryClose(InventoryCloseEvent e) {}

    @Deprecated
    public void setItem(UIElement uiElement, ItemStack item, Player p) {
        for(UIElement uie : baseUIElements) {
            if(uie.getID(p).equals(uiElement.getID(p))) {
                getInvGUI(p).setItem(uie.slot, item);
                updateInventory(p);
                break;
            }
        }
    }

    public void setSlot(int slot, ItemStack item, Player p) {
        Inventory inv = getInvGUI(p);
        if (inv != null) {
            inv.setItem(slot, item);
            p.updateInventory();
        }
    }

    public void updateInventory(Player p) {
        p.updateInventory();
    }

    public Inventory getInvGUI(Player p) {
        return inventories.get(p.getUniqueId());
    }

    public void setContents(Player p, Inventory inv) {
        for (UIElement uie : baseUIElements) {
            inv.setItem(uie.getSlot(), uie.getItemStack(p));
        }

        if (emptyFill != null) {
            for (int i = 0; i < inv.getSize(); i++) {
                if (inv.getItem(i) == null) {
                    inv.setItem(i, emptyFill);
                }
            }
        }
    }

    public void refreshElement(Player p, String elementID) {
        Inventory inv = getInvGUI(p);
        if (inv != null) {
            for (UIElement uie : baseUIElements) {
                if (uie.getID(p).equals(elementID)) {
                    inv.setItem(uie.slot, uie.getItemStack(p));
                    break;
                }
            }
        }
    }

    public String getID() {
        return this.ID;
    }

    public static class Builder {
        List<UIElement> uiElements = new ArrayList<>();
        ItemStack emptyFill = null;

        boolean cancelsInteraction = false;
        int invSize;
        int unicode = 0;
        String interfaceID;
        String negativeSpacesTitle = "";
        String negativeSpacesGui = "";
        String title;
        TextColor color = NamedTextColor.DARK_GRAY;

        public Builder(String interfaceID, int invSize, String title) {
            this.interfaceID = interfaceID;
            this.invSize = invSize;
            this.title = title;
        }

        public Builder cancelsInteraction(boolean value) {
            this.cancelsInteraction = value;
            return this;
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

        public Builder setGUIUnicode(int unicode) {
            this.unicode = unicode;
            return this;
        }

        public Builder setGUINegativeSpaces(int value) {
            negativeSpacesGui = MiscUtil.getNegativeSpaces(value);
            return this;
        }

        public Builder setTitleNegativeSpaces(int value) {
            negativeSpacesTitle = MiscUtil.getNegativeSpaces(value);
            return this;
        }

        public Builder setGUITitleColor(TextColor color) {
            this.color = color;
            return this;
        }

        public InvUserInterface build() {
            return new InvUserInterface(this);
        }
    }
}
