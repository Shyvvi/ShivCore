package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    public static List<Player> PLAYERS_ONLINE = new ArrayList<>();

    public static List<Player> getPlayersOnline() {
        return new ArrayList<>(ShivCore.getInstance().getServer().getOnlinePlayers());
    }

    public static void initialize() {
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(new PlayerListener(), ShivCore.getInstance());
        PLAYERS_ONLINE = getPlayersOnline();
        for(Player p : PLAYERS_ONLINE) {
            p.closeInventory();
        }
    }
}
