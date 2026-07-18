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

public class DamageListener implements Listener {

    @EventHandler
    public void damageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        int customTypeInt = EntityUtil.getInt(entity, "customDamageType");
        DamageType damageType = DamageManager.damageTypeList.get(customTypeInt);

        if (customTypeInt != 0 && damageType != null && entity instanceof LivingEntity le && le.getHealth() - e.getFinalDamage() > 0) {
            Bukkit.getScheduler().runTaskLater(ShivCore.getInstance(), () -> {
                EntityUtil.setInt(entity, 0, "customDamageType");
            }, 1);
        }
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        int customTypeInt = EntityUtil.getInt(player, "customDamageType");
        DamageType damageType = DamageManager.damageTypeList.get(customTypeInt);

        if (customTypeInt != 0 && damageType != null) {
            e.deathMessage(Component.text(player.getName() + " " + damageType.getDeathMessage()));
            EntityUtil.setInt(player, 0, "customDamageType");
        }
    }
}
