package net.shyvv.shivCore.util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void playersJoin(PlayerJoinEvent e) {
        PlayerManager.PLAYERS_ONLINE.add(e.getPlayer());
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        PlayerManager.PLAYERS_ONLINE.remove(e.getPlayer());
    }
}
