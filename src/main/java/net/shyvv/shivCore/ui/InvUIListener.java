package net.shyvv.shivCore.ui;

import net.shyvv.shivCore.util.managers.GUIManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

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

    private <T extends InventoryEvent> void callEvent(String methodID, T inventoryEvent) {
        for(InvUserInterface iui : GUIManager.USER_INTERFACES) {
            if(iui.inventory != null && iui.inventory.equals(inventoryEvent.getInventory())) {
                try {
                    iui.getClass().getDeclaredMethod(methodID, inventoryEvent.getClass()).invoke(iui, inventoryEvent);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
