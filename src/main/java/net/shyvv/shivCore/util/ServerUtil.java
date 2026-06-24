package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.command.Command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerUtil {

    public static final String OPEN_UI_CMD = "openui";
    public static final String GIVE_ITEM_CMD = "giveitem";
    private static final Map<String, AtomicInteger> atomicIntegers = new ConcurrentHashMap<>();
    private static final Map<String, AtomicBoolean> atomicBooleans = new ConcurrentHashMap<>();


    public static void registerCommand(Command cmdClass) {
        try {
            ShivCore.getInstance().getServer().getCommandMap().register("shivcore", cmdClass);
            ShivCore.log.info("Registered command {}", cmdClass.getName());
        } catch(Exception e) {
            ShivCore.log.error("Failed to register command {}", cmdClass.getName());
        }
    }

    public static int incrementInt(String id) {
        return atomicIntegers.computeIfAbsent(id, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public static int decrementInt(String id) {
        return atomicIntegers.computeIfAbsent(id, k -> new AtomicInteger(0)).decrementAndGet();
    }

    public static int getInt(String id) {
        AtomicInteger value = atomicIntegers.get(id);
        if(value != null) return value.get();
        return 0;
    }

    public static void setInt(String id, int value) {
        atomicIntegers.computeIfAbsent(id, k -> new AtomicInteger(0)).set(value);
    }

    public static void deleteInteger(String id) {
        atomicIntegers.remove(id);
    }

    public static boolean getBoolean(String id) {
        AtomicBoolean value = atomicBooleans.get(id);
        return value.get();
    }

    public static void setBoolean(String id, boolean value) {
        atomicBooleans.computeIfAbsent(id, k -> new AtomicBoolean(false)).set(value);
    }

    public static void deleteBoolean(String id) {
        atomicBooleans.remove(id);
    }
}
