package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCoreMain;
import net.shyvv.shivCore.entities.AbstractTickingEntity;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
    //Key used to determine whether an entity is supposed to be ticked or not
    public static NamespacedKey TICKING_KEY = new NamespacedKey(ShivCoreMain.getInstance(), "tickingKey");
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
        }.runTaskTimer(ShivCoreMain.getInstance(), 10, 20);
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
        e.setMetadata("isTicking", new FixedMetadataValue(ShivCoreMain.getInstance(), true));
        new BukkitRunnable() {
            @Override
            public void run() {
                AbstractTickingEntity entityClass = getTickingEntityClass(e);
                if(entityClass != null) {
                    entityClass.primaryTick(e);
                } else {
                    e.removeMetadata("isTicking", ShivCoreMain.getInstance());
                    this.cancel();
                }

                if(e.isDead()) {
                    e.removeMetadata("isTicking", ShivCoreMain.getInstance());
                    this.cancel();
                }
            }
        }.runTaskTimer(ShivCoreMain.getInstance(), 0, 1);
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
        }.runTaskTimer(ShivCoreMain.getInstance(), 1, 200);
    }

    /// Set a boolean to an entity's PersistentDataContainer
    public static void setBoolean(Entity entity, boolean value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.BOOLEAN, value);
    }

    /// Get a boolean from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static boolean getBoolean(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.BOOLEAN);
        }
        return false;
    }

    /// Set an integer to an entity's PersistentDataContainer
    public static void setInt(Entity entity, int value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.INTEGER, value);
    }

    /// Get an integer from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static int getInt(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.INTEGER);
        }
        return 0;
    }

    /// Set a float to an entity's PersistentDataContainer
    public static void setFloat(Entity entity, float value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.FLOAT, value);
    }

    /// Get a float from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static float getFloat(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.FLOAT);
        }
        return 0;
    }
}
