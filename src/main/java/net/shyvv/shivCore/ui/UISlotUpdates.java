package net.shyvv.shivCore.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public interface UISlotUpdates {
    void onUpdate(Player p, InventoryClickEvent e);
}
