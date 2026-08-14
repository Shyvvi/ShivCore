package net.shyvv.shivCore.effects;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.util.EphemeralEntityUtil;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.eclipse.sisu.Nullable;

import java.util.function.BiConsumer;

public record SCPotionEffect(String id, PotionEffectType override, BiConsumer<LivingEntity, Integer> tickFunction, BiConsumer<LivingEntity, Integer> finishFunction, TriConsumer<LivingEntity, EntityDamageEvent, Integer> damageFunction) implements Listener {
    public SCPotionEffect {
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(this, ShivCore.getInstance());
    }
    public SCPotionEffect(String id, @Nullable PotionEffectType override, BiConsumer<LivingEntity, Integer> tickFunction, BiConsumer<LivingEntity, Integer> finishFunction) {
        this(id, override, tickFunction, finishFunction, null);
    }
    public SCPotionEffect(String id, @Nullable PotionEffectType override, TriConsumer<LivingEntity, EntityDamageEvent, Integer> damageFunction) {
        this(id, override, null, null, damageFunction);
    }

    public void addPotionEffect(LivingEntity p, int duration, int level) {
        if (getType() != null) p.addPotionEffect(new PotionEffect(getType(), duration, level, false, true, true));
        EphemeralEntityUtil.setEphemeralInt(p, duration, id() + "PotionTicking");
        EphemeralEntityUtil.setEphemeralInt(p, level, id() + "PotionLevel");
        Bukkit.getScheduler().runTaskTimer(ShivCore.getInstance(), bukkitTask -> {
            int i = EphemeralEntityUtil.decrementEphemeralInt(p, id() + "PotionTicking");
            if (!p.isValid()) {
                bukkitTask.cancel();
                return;
            }
            if (tickFunction != null) tickFunction.accept(p, level);
            if (i <= 0 || EphemeralEntityUtil.getEphemeralBoolean(p, id()+"PotionCancelled")) {
                if (finishFunction != null) finishFunction.accept(p, level);
                bukkitTask.cancel();
            }
        }, 1, 1);
    }

    public void removePotionEffect(LivingEntity p) {
        EphemeralEntityUtil.setEphemeralBoolean(p, true, id() + "PotionCancelled");
    }

    public boolean hasPotionEffect(LivingEntity p) {
        return EphemeralEntityUtil.getEphemeralInt(p, id()+"PotionTicking") > 0;
    }

    public PotionEffectType getType() {
        return override();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof LivingEntity p && hasPotionEffect(p) && damageFunction != null) {
            damageFunction.accept(p, e, EphemeralEntityUtil.getEphemeralInt(p, id()+"PotionLevel"));
        }
    }
}
