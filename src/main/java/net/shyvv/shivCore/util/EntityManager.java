package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.entities.AbstractTickingEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    //Key used to determine whether an entity is supposed to be ticked or not
    public static NamespacedKey TICKING_KEY = new NamespacedKey(ShivCore.getInstance(), "tickingKey");
    //List of different
    public static List<AbstractTickingEntity> TICKING_ENTITIES = new ArrayList<>();

    public static void initialize() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (AbstractTickingEntity entity : TICKING_ENTITIES) {
                        entity.init();
                    }
                    this.cancel();
                    initEntitySearcher();
                } catch (Exception ignored) {}
            }
        }.runTaskTimer(ShivCore.getInstance(), 10, 20);
    }

    private static AbstractTickingEntity getTickingEntityClass(Entity e) {
        for (AbstractTickingEntity entityClass : TICKING_ENTITIES) {
            if (e.getPersistentDataContainer().has(entityClass.getEntityID(), PersistentDataType.BOOLEAN)) {
                try {
                    return entityClass;
                } catch(Exception ignored) {}
            }
        }
        return null;
    }

    public static void beginTickingEntity(Entity e) {
        e.setMetadata("isTicking", new FixedMetadataValue(ShivCore.getInstance(), true));
        new BukkitRunnable() {
            @Override
            public void run() {
                AbstractTickingEntity entityClass = getTickingEntityClass(e);
                if(entityClass != null) {
                    entityClass.primaryTick(e);
                } else {
                    e.removeMetadata("isTicking", ShivCore.getInstance());
                    this.cancel();
                }

                if(e.isDead()) {
                    e.removeMetadata("isTicking", ShivCore.getInstance());
                    this.cancel();
                }
            }
        }.runTaskTimer(ShivCore.getInstance(), 0, 1);
    }

    public static boolean entityIsTicking(Entity e) {
        return e.hasMetadata("isTicking");
    }

    private static void initEntitySearcher() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if(entity.getPersistentDataContainer().has(TICKING_KEY) && !entityIsTicking(entity)) {
                            beginTickingEntity(entity);
                        }
                    }
                }
            }
        }.runTaskTimer(ShivCore.getInstance(), 1, 200);
    }
}
