package net.shyvv.shivCore.util;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EphemeralEntityUtil implements Listener {
    private static final Map<UUID, ConcurrentHashMap<String, Boolean>> ephemeralBooleans = new ConcurrentHashMap<>();
    private static final Map<UUID, ConcurrentHashMap<String, Integer>> ephemeralInts = new ConcurrentHashMap<>();
    private static final Map<UUID, ConcurrentHashMap<String, Double>> ephemeralDoubles = new ConcurrentHashMap<>();
    private static final Map<UUID, ConcurrentHashMap<String, String>> ephemeralStrings = new ConcurrentHashMap<>();

    public static void setBoolean(Entity entity, boolean value, String id) {
        ephemeralBooleans.computeIfAbsent(entity.getUniqueId(), k -> new ConcurrentHashMap<>()).put(id, value);
    }

    public static boolean getBoolean(Entity entity, String id) {
        ConcurrentHashMap<String, Boolean> map = ephemeralBooleans.get(entity.getUniqueId());
        if (map != null) {
            return map.getOrDefault(id, false);
        }
        return false;
    }

    public static void setString(Entity entity, String value, String id) {
        ephemeralStrings.computeIfAbsent(entity.getUniqueId(), k -> new ConcurrentHashMap<>()).put(id, value);
    }

    public static String getString(Entity entity, String id) {
        ConcurrentHashMap<String, String> map = ephemeralStrings.get(entity.getUniqueId());
        if (map != null) {
            return map.getOrDefault(id, "");
        }
        return "";
    }

    public static void setInt(Entity entity, int value, String id) {
        ephemeralInts.computeIfAbsent(entity.getUniqueId(), k -> new ConcurrentHashMap<>()).put(id, value);
    }

    public static int getInt(Entity entity, String id) {
        ConcurrentHashMap<String, Integer> map = ephemeralInts.get(entity.getUniqueId());
        if (map != null) {
            return map.getOrDefault(id, 0);
        }
        return 0;
    }

    public static void setDouble(Entity entity, double value, String id) {
        ephemeralDoubles.computeIfAbsent(entity.getUniqueId(), k -> new ConcurrentHashMap<>()).put(id, value);
    }

    public static double getDouble(Entity entity, String id) {
        ConcurrentHashMap<String, Double> map = ephemeralDoubles.get(entity.getUniqueId());
        if (map != null) {
            return map.getOrDefault(id, 0.0);
        }
        return 0;
    }

    public static int incrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i + 1, id);
        return i;
    }

    public static int decrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i - 1, id);
        return i;
    }

    public static void clearEphemeralData(Entity entity) {
        UUID id = entity.getUniqueId();
        ephemeralBooleans.remove(id);
        ephemeralInts.remove(id);
        ephemeralStrings.remove(id);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        clearEphemeralData(e.getPlayer());
    }
}