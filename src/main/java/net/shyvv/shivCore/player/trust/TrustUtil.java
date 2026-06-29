package net.shyvv.shivCore.player.trust;

import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class TrustUtil {
    public static void setPlayerTrusted(Player truster, Player trusted, boolean value) {
        List<String> l = EntityUtil.getStringList(truster, "trustedPlayers");
        if(l != null) {
            if (value) l.add(trusted.getName());
            else l.remove(trusted.getName());
        }
        EntityUtil.setStringList(truster, l, "trustedPlayers");
    }

    public static boolean playerIsTrusted(Player truster, Player trusted) {
        List<String> l = EntityUtil.getStringList(truster, "trustedPlayers");
        return l != null && l.contains(trusted.getName());
    }
}
