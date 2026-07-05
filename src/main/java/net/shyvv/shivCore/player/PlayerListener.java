package net.shyvv.shivCore.player;

import net.shyvv.shivCore.util.CooldownUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Consumer;

public class PlayerListener implements Listener {

    @EventHandler
    public void playersJoin(PlayerJoinEvent e) {
        PlayerManager.PLAYERS_ONLINE.add(e.getPlayer());
        CooldownUtil.COOLDOWN_IDS.forEach(s -> CooldownUtil.resetCooldown(e.getPlayer(), s));
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        PlayerManager.PLAYERS_ONLINE.remove(e.getPlayer());
    }
}
