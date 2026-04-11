package net.shyvv.shivCore.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;

public class InvUIListener implements Listener {

    @EventHandler
    private void inventoryClickEvent(InventoryClickEvent e) {
        callEvent("onInventoryClick", e);
    }

    @EventHandler
    private void inventoryInteractEvent(InventoryInteractEvent e) {
        callEvent("onInventoryInteract", e);
    }

    @EventHandler
    private void inventoryCloseEvent(InventoryCloseEvent e) {
        callEvent("onInventoryClose", e);
    }

    @EventHandler
    private void inventoryOpenEvent(InventoryOpenEvent e) {
        callEvent("onInventoryOpen", e);
    }

    private <T extends InventoryEvent> void callEvent(String methodID, T inventoryEvent) {
        if ((inventoryEvent.getView().getPlayer() instanceof Player player)) {
            for (InvUserInterface iui : GUIManager.USER_INTERFACES) {
                Inventory playerInv = iui.getInvGUI(player);
                if (playerInv != null && playerInv.equals(inventoryEvent.getInventory())) {
                    try {
                        iui.getClass().getDeclaredMethod(methodID, inventoryEvent.getClass()).invoke(iui, inventoryEvent);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException("Failed to invoke UI event: " + methodID, e);
                    }
                }
            }
        }
    }
}
