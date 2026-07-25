package net.shyvv.shivCore.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface UIClickable {
    boolean clickConditions(InventoryClickEvent e);
    void onClick(Player p);
}
