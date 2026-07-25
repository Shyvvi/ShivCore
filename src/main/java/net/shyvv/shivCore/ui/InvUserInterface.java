package net.shyvv.shivCore.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.item.ItemStackBuilder;
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
    List<Integer> forceEmpty;
    ItemStack emptyFill;
    private InvUserInterface(Builder builder) {
        this.ID = builder.interfaceID;
        this.invSize = builder.invSize;
        this.title = Component.text().append(Component.text(builder.negativeSpacesGui + Character.toString(builder.unicode), NamedTextColor.WHITE)).append(Component.text(builder.negativeSpacesTitle + builder.title, builder.color)).build();
        ShivCore.log.info("Interface {} with unicode value of: {}", builder.interfaceID, Character.toString(builder.unicode));
        this.baseUIElements = builder.uiElements;
        this.emptyFill = builder.emptyFill;
        this.unicode = builder.unicode;
        this.cancelsInteraction = builder.cancelsInteraction;
        this.forceEmpty = builder.forceEmpty;
        GUIManager.USER_INTERFACES.add(this);
    }

    public void openUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, invSize, title);
        inventories.put(p.getUniqueId(), inv);
        setContents(p, inv);
        p.openInventory(inv);
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if(cancelsInteraction) {
            e.setCancelled(true);
        }
        ItemStack clicked = e.getCurrentItem();
        HumanEntity entity = e.getWhoClicked();
        if(entity instanceof Player p) {
            for (UIElement uie : baseUIElements) {
                if (uie instanceof UISlotUpdates uisu) {
                    uisu.onUpdate(p, e);
                }
            }
            if(clicked != null) {
                if(clicked.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), "CANCELS_INTERACTION"))) {
                    e.setCancelled(true);
                }
                if(e.getInventory().equals(getInvGUI(p))) {
                    for (UIElement uie : baseUIElements) {
                        if (getItemID(clicked).equals(uie.getID(p))) {
                            if(uie instanceof UIClickable uir) {
                                if(uir.clickConditions(e)) uir.onClick(p);
                                e.setCancelled(true);
                            } else if(uie instanceof UICancelsInteraction) {
                                e.setCancelled(true);
                            }
                            break;
                        }
                    }
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

    public void onInventoryOpen(InventoryOpenEvent e) {}

    public void onInventoryClose(InventoryCloseEvent e) {
        if(!cancelsInteraction) {
            if(e.getPlayer() instanceof Player p) {
                Inventory inv = e.getInventory();
                List<ItemStack> leftovers = new ArrayList<>();

                for (int i = 0; i < inv.getSize(); i++) {
                    ItemStack item = inv.getItem(i);
                    if (item != null && forceEmpty.contains(i)) {
                        HashMap<Integer, ItemStack> currentLeftovers = p.getInventory().addItem(item);
                        if (!currentLeftovers.isEmpty()) {
                            leftovers.addAll(currentLeftovers.values());
                        }
                    }
                }

                if (!leftovers.isEmpty()) {
                    for (ItemStack drop : leftovers) {
                        p.getWorld().dropItemNaturally(p.getLocation(), drop);
                    }
                }
                p.updateInventory();

            }
        }
    }

    public void setSlot(UIElement uiElement, Player p) {
        for(UIElement uie : baseUIElements) {
            if(uie.getID(p).equals(uiElement.getID(p))) {
                getInvGUI(p).setItem(uie.slot, uie.getItemStack(p));
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
                if (inv.getItem(i) == null && !forceEmpty.contains(i)) {
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
        List<Integer> forceEmpty = new ArrayList<>();
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

        /**
         * Designates a slot to be empty, !! IMPORTANT !! when an inventory is closed only items in these slots will be given back to the player
         * @param list List of slots that will be empty
         * @return Builder
         */
        public Builder forceEmpty(List<Integer> list) {
            forceEmpty.addAll(list);
            return this;
        }

        public Builder fillEmpty(ItemStack itemStack) {
            emptyFill = itemStack;
            return this;
        }

        public Builder fillEmptyNonInteractable(ItemStack itemStack) {
            emptyFill = ItemStackBuilder.setItemStackData(itemStack, "CANCELS_INTERACTION", PersistentDataType.BOOLEAN, true);
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
