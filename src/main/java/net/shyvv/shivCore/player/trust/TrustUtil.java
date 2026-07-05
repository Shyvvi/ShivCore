package net.shyvv.shivCore.player.trust;

import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TrustUtil {
    public static void setPlayerTrusted(Player truster, String trustedName, boolean value) {
        List<String> existing = EntityUtil.getStringList(truster, "trustedPlayers");
        List<String> l = existing == null ? new ArrayList<>() : new ArrayList<>(existing);

        if (value) {
            if (!l.contains(trustedName)) {
                l.add(trustedName);
            }
        } else {
            l.remove(trustedName);
        }

        EntityUtil.setStringList(truster, l, "trustedPlayers");
    }

    public static void resetTrustedPlayers(Player p) {
        EntityUtil.setStringList(p, new ArrayList<>(), "trustedPlayers");
    }

    public static boolean playerIsTrusted(Player truster, String trustedName) {
        List<String> l = EntityUtil.getStringList(truster, "trustedPlayers");
        return l != null && l.contains(trustedName);
    }

    public static List<String> getTrustedPlayers(Player truster) {
        return EntityUtil.getStringList(truster, "trustedPlayers");
    }
}
