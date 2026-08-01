package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
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

    public static void setEphemeralBoolean(Entity entity, boolean value, String id) {
        ephemeralBooleans.computeIfAbsent(entity.getUniqueId(), k -> new ConcurrentHashMap<>()).put(id, value);
    }

    public static boolean getEphemeralBoolean(Entity entity, String id) {
        ConcurrentHashMap<String, Boolean> map = ephemeralBooleans.get(entity.getUniqueId());
        if (map != null) {
            return map.getOrDefault(id, false);
        }
        return false;
    }

    public static void setEphemeralInt(Entity entity, int value, String id) {
        ephemeralInts.computeIfAbsent(entity.getUniqueId(), k -> new ConcurrentHashMap<>()).put(id, value);
    }

    public static int getEphemeralInt(Entity entity, String id) {
        ConcurrentHashMap<String, Integer> map = ephemeralInts.get(entity.getUniqueId());
        if (map != null) {
            return map.getOrDefault(id, 0);
        }
        return 0;
    }

    public static int incrementEphemeralInt(Entity entity, String id) {
        int i = getEphemeralInt(entity, id);
        setEphemeralInt(entity, i + 1, id);
        return i;
    }

    public static int decrementEphemeralInt(Entity entity, String id) {
        int i = getEphemeralInt(entity, id);
        setEphemeralInt(entity, i - 1, id);
        return i;
    }

    public static void clearEphemeralData(Entity entity) {
        UUID id = entity.getUniqueId();
        ephemeralBooleans.remove(id);
        ephemeralInts.remove(id);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        System.out.print("TEST");
        clearEphemeralData(e.getPlayer());
    }
}