package net.shyvv.shivCore.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class ItemListener implements Listener {

    @EventHandler
    private void playerInteractEvent(PlayerInteractEvent e) {
        callEvent("onPlayerInteract", e);
    }

    @EventHandler
    private void playerInteractEntityEvent(PlayerInteractEntityEvent e) {
        callEvent("onPlayerEntityInteract", e);
    }

    private <T extends PlayerEvent> void callEvent(String methodID, T playerInteractEvent) {
        ItemStack stack = null;
        if(playerInteractEvent instanceof PlayerInteractEvent e) {
            stack = e.getItem();
        } else if(playerInteractEvent instanceof PlayerInteractEntityEvent e) {
            stack = e.getPlayer().getActiveItem();
        }
        if(stack != null) {
            for(SCItem sci : ItemManager.CUSTOM_ITEMS) {
                if(sci.matchesItemID(stack)) {
                    try {
                        sci.getClass().getDeclaredMethod(methodID, playerInteractEvent.getClass()).invoke(sci, playerInteractEvent);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
