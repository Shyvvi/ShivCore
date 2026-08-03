package net.shyvv.shivCore.util;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.entities.AbstractTickingEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager {

    public static final String TICKING_KEY = "tickingKey";
    public static final List<AbstractTickingEntity> TICKING_ENTITIES = new ArrayList<>();
    public static Map<String, AbstractTickingEntity> TICKING_ENTITY_LOOKUP = new ConcurrentHashMap<>();

    public static void initialize() {
        Bukkit.getScheduler().runTaskLater(ShivCore.getInstance(), () -> {
            sweepLoadedEntities();
        },1);

        Bukkit.getPluginManager().registerEvents(new TickingEntityListener(), ShivCore.getInstance());
    }

    private static void sweepLoadedEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if(EntityUtil.getBoolean(entity, TICKING_KEY)) tryBeginTicking(entity);
            }
        }
    }

    private static void tryBeginTicking(Entity entity) {
        if (!entityIsTicking(entity)) {
            AbstractTickingEntity entityClass = resolveTickingEntityClass(entity);
            if (entityClass != null) {
                beginTickingEntity(entity, entityClass);
            }
        }
    }

    private static AbstractTickingEntity resolveTickingEntityClass(Entity e) {
        String key = EntityUtil.getString(e, "ENTITY_IDENTIFICATION");
        if(TICKING_ENTITY_LOOKUP.containsKey(key)) {
            return TICKING_ENTITY_LOOKUP.get(key);
        }
        return null;
    }

    public static void beginTickingEntity(Entity e, AbstractTickingEntity entityClass) {
        EntityUtil.setBoolean(e, true, "isTicking");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.isDead() || !e.isValid()) {
                    EntityUtil.setBoolean(e, false, "isTicking");
                    this.cancel();
                    return;
                }
                entityClass.primaryTick(e);
            }
        }.runTaskTimer(ShivCore.getInstance(), 0, 1);
    }

    public static boolean entityIsTicking(Entity e) {
        return EphemeralEntityUtil.getEphemeralBoolean(e, "isCurrentlyTicking");
    }

    // this is fine I guess, trying my best to keep shit organized
    private static class TickingEntityListener implements Listener {

        @EventHandler
        public void onEntitiesLoad(EntitiesLoadEvent event) {
            for (Entity entity : event.getEntities()) {
                tryBeginTicking(entity);
            }
        }
    }
}
