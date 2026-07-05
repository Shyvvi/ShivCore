package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CooldownUtil {
    public static List<String> COOLDOWN_IDS = new ArrayList<>();
    public static void setCooldown(Player player, String cooldownKey, int cd) {
        int hasCd = getCooldown(player, cooldownKey);
        player.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), cooldownKey), PersistentDataType.INTEGER, cd);
        if(hasCd == 0) {
            initRunnable(player, cooldownKey);
        }
        if(!COOLDOWN_IDS.contains(cooldownKey)) COOLDOWN_IDS.add(cooldownKey);
    }

    public static void resetCooldown(Player player, String cooldownKey) {
        player.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), cooldownKey), PersistentDataType.INTEGER, 0);
    }

    public static int getCooldown(Player player, String cooldownKey) {
        NamespacedKey namespace = new NamespacedKey(ShivCore.getInstance(), cooldownKey);
        if(player.getPersistentDataContainer().has(namespace)) {
            return player.getPersistentDataContainer().get(namespace, PersistentDataType.INTEGER);
        }
        return 0;
    }

    private static void initRunnable(Player p, String cooldownKey) {
        NamespacedKey namespace = new NamespacedKey(ShivCore.getInstance(), cooldownKey);
        new BukkitRunnable() {
            @Override
            public void run() {
                int cd = getCooldown(p, cooldownKey);

                if(cd <= 0) {
                    p.getPersistentDataContainer().remove(namespace);
                    this.cancel();
                }

                if(cd > 0 && !this.isCancelled()) {
                    cd--;
                    setCooldown(p, cooldownKey, cd);
                }

                if(cd <= 0) {
                    p.getPersistentDataContainer().remove(namespace);
                    this.cancel();
                }
                //nvm it works now I don't hate it <3
            }
        }.runTaskTimer(ShivCore.getInstance(), 20, 20);
    }
}
