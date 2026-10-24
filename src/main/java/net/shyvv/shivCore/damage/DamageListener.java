package net.shyvv.shivCore.damage;

import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class DamageListener implements Listener {

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        int storedTypeInt = EntityUtil.getInt(player, "customDamageType");
        if (storedTypeInt > 0) {
            int realIndex = storedTypeInt - 1;

            if (realIndex < DamageManager.damageTypeList.size()) {
                DamageType damageType = DamageManager.damageTypeList.get(realIndex);
                if (damageType != null) {
                    e.deathMessage(Component.text(player.getName() + " " + damageType.getDeathMessage()));
                }
            }
        }
        EntityUtil.setInt(player, 0, "customDamageType");
    }
}
