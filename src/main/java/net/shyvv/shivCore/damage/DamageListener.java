package net.shyvv.shivCore.damage;

import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DamageListener implements Listener, EntityUtils {

    @EventHandler
    public void damageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        DamageType damageType = DamageManager.damageTypeList.get(getInt(entity, "customDamageType"));
        Bukkit.getScheduler().runTaskLater(ShivCore.getInstance(), () -> {
            if(entity instanceof LivingEntity le) {
                if(damageType.ignoresIFrames) {
                    le.setNoDamageTicks(0);
                }
            }
            setInt(entity, 0, "customDamageType");
        }, 1);
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        e.getPlayer().sendMessage(Component.text(getInt(e.getPlayer(), "customDamageType")));
        if(getInt(e.getPlayer(), "customDamageType") != 0) {
            DamageType damageType = DamageManager.damageTypeList.get(getInt(e.getPlayer(), "customDamageType"));
            e.deathMessage(Component.text(e.getPlayer().getName() + " " + damageType.getDeathMessage()));
        }
    }
}
