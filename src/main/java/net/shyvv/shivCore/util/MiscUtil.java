package fr.stormer3428.shivcore.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class MiscUtil {
    /// This class is just yoinked from the trial plugin which I made

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static float getPythagoreas(Vector loc1, Vector loc2) {
        return (float) Math.sqrt(Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2) + Math.pow(Math.abs(loc1.getY() - loc2.getY()), 2) + Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2));
    }

    public static float getPythagoreas(Vector loc1) {
        return (float) Math.sqrt(Math.pow(Math.abs(loc1.getX()), 2) + Math.pow(Math.abs(loc1.getY()), 2) + Math.pow(Math.abs(loc1.getZ()), 2));
    }

    public static Vector getDirectionBetweenVectors(Location loc1, Location loc2) {
        return loc1.clone().subtract(loc2).toVector();
    }

    public static int useTime(int usetime) {
        return Math.abs(usetime-72000);
    }

    public static float getVariation(float v1, float v2) {
        Random rand = new Random();
        return v1+rand.nextFloat(-v2, v2);
    }

    public static void debugMessage(String msg) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    ///Confusing code courtesy of Wasabi_Thumbs off Spigot forums, modified by Shyvv
    private static final double EPSILON = Math.ulp(1.0d) * 2d;
    private static boolean isSignificant(double value) {
        return Math.abs(value) >= EPSILON;
    }
    public static Location getRelativeLocation(Location loc, double forward, double right, double up, float setYaw, float setPitch) {
        Location ret = loc;

        if(setPitch != 0) {
            ret = new Location(ret.getWorld(), ret.getX(), ret.getY(), ret.getZ(), ret.getYaw(), setPitch);
        }
        if(setYaw != 0) {
            ret = new Location(ret.getWorld(), ret.getX(), ret.getY(), ret.getZ(), setYaw, ret.getPitch());
        }
        Vector direction = null;
        if (isSignificant(forward)) {
            direction = ret.getDirection();
            ret.add(direction.clone().multiply(forward));
        }
        boolean hasUp = isSignificant(up);
        if (hasUp && direction == null) direction = ret.getDirection();
        if (isSignificant(right) || hasUp) {
            Vector rightDirection;
            if (direction != null && isSignificant(Math.abs(direction.getY()) - 1)) {
                rightDirection = direction.clone();
                double factor = Math.sqrt(1 - Math.pow(rightDirection.getY(), 2)); // a shortcut that lets us not normalize which is slow
                double nx = -rightDirection.getZ() / factor;
                double nz = rightDirection.getX() / factor;
                rightDirection.setX(nx);
                rightDirection.setY(0d);
                rightDirection.setZ(nz);
            } else {
                float yaw = ret.getYaw() + 90f;
                double yawRad = yaw * (Math.PI / 180d);
                double z = Math.cos(yawRad);
                double x = -Math.sin(yawRad);
                rightDirection = new Vector(x, 0d, z);
            }
            ret.add(rightDirection.clone().multiply(right));
            if (hasUp) {
                Vector upDirection = rightDirection.crossProduct(direction);
                ret.add(upDirection.clone().multiply(up));
            }
        }
        return ret;
    }

    public static Location getRelativeLocation(Location loc, double forward, double right, double up) {
        return getRelativeLocation(loc, forward, right, up, 0, 0);
    }
}
