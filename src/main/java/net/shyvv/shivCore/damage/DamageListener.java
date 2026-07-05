package net.shyvv.shivCore.damage;

import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void damageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        DamageType damageType = DamageManager.damageTypeList.get(EntityUtil.getInt(entity, "customDamageType"));
        Bukkit.getScheduler().runTaskLater(ShivCore.getInstance(), () -> {
            if(entity instanceof LivingEntity le) {
                if(damageType.ignoresIFrames) {
                    le.setNoDamageTicks(0);
                }
            }
            EntityUtil.setInt(entity, 0, "customDamageType");
        }, 1);
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        e.getPlayer().sendMessage(Component.text(EntityUtil.getInt(e.getPlayer(), "customDamageType")));
        if(EntityUtil.getInt(e.getPlayer(), "customDamageType") != 0) {
            DamageType damageType = DamageManager.damageTypeList.get(EntityUtil.getInt(e.getPlayer(), "customDamageType"));
            e.deathMessage(Component.text(e.getPlayer().getName() + " " + damageType.getDeathMessage()));
        }
    }
}
