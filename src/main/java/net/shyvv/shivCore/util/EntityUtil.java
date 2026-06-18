package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

@Deprecated
public class EntityUtil {
    /// Set a boolean to an entity's PersistentDataContainer
    public static void setBoolean(Entity entity, boolean value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.BOOLEAN, value);
    }

    /// Get a boolean from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static boolean getBoolean(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.BOOLEAN);
        }
        return false;
    }

    public static int incrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i+1, id);
        return i;
    }

    /// Set an integer to an entity's PersistentDataContainer
    public static void setInt(Entity entity, int value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.INTEGER, value);
    }

    /// Get an integer from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static int getInt(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.INTEGER);
        }
        return 0;
    }

    /// Set a float to an entity's PersistentDataContainer
    public static void setFloat(Entity entity, float value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.FLOAT, value);
    }

    /// Get a float from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static float getFloat(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.FLOAT);
        }
        return 0;
    }
}
