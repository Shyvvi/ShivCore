package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

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

    public static int decrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i-1, id);
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

    /// Set a String List to an entity's PersistentDataContainer
    public static void setStringList(Entity entity, List<String> value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.LIST.strings(), value);
    }

    /// Get a String List from an entity's PersistentDataContainer
    @SuppressWarnings("DataFlowIssue")
    public static List<String> getStringList(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.LIST.strings());
        }
        return null;
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

    /// Set a string to an entity's PersistentDataContainer
    public static void setString(Entity entity, String value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.STRING, value);
    }

    /// Get a string from an entity's PersistentDataContainer
    public static String getString(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.STRING);
        }
        return "";
    }

    public static void storeLocation(Entity entity, Location loc, String locationKey) {
        setFloat(entity, (float) loc.getX(), "entityXCoordinate"+locationKey);
        setFloat(entity, (float) loc.getY(), "entityYCoordinate"+locationKey);
        setFloat(entity, (float) loc.getZ(), "entityZCoordinate"+locationKey);
        setFloat(entity, loc.getPitch(), "entityPitchCoordinate"+locationKey);
        setFloat(entity, loc.getYaw(), "entityYawCoordinate"+locationKey);
    }

    public static Location getStoredLocation(Entity entity, String locationKey) {
        return new Location(entity.getWorld(),
                getFloat(entity, "entityXCoordinate"+locationKey),
                getFloat(entity, "entityYCoordinate"+locationKey),
                getFloat(entity, "entityZCoordinate"+locationKey),
                getFloat(entity, "entityPitchCoordinate"+locationKey),
                getFloat(entity, "entityYawCoordinate"+locationKey)
        );
    }

    public static boolean hasStoredLocation(Entity entity, String locationKey) {
        Location l = getStoredLocation(entity, locationKey);
        return !(l.getX() != 0 && l.getY() != 0 && l.getZ() != 0);
    }

    public static boolean hasMoved(Entity entity) {
        if(hasStoredLocation(entity, "hasMoved")) {
            Location oldLoc = getStoredLocation(entity, "hasMoved");
            Location prsLoc = entity.getLocation();
            Location locDif = prsLoc.subtract(oldLoc);
            return Math.abs(locDif.getX()) > 0.05 && Math.abs(locDif.getY()) > 0.05 && Math.abs(locDif.getZ()) > 0.05;
        }
        storeLocation(entity, entity.getLocation(), "hasMoved");
        return false;
    }
}
