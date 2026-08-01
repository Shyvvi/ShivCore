package net.shyvv.shivCore.util;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

import java.util.Random;

public class MiscUtil {

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static float getPythagoreas(Vector loc1, Vector loc2) {
        return (float) Math.sqrt(Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2) + Math.pow(Math.abs(loc1.getY() - loc2.getY()), 2) + Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2));
    }

    public static float getPythagoreas(Vector loc1) {
        return (float) Math.sqrt(Math.pow(Math.abs(loc1.getX()), 2) + Math.pow(Math.abs(loc1.getY()), 2) + Math.pow(Math.abs(loc1.getZ()), 2));
    }

    public static boolean isBehindEntity(Entity entity1, Entity entity2, int yawDifference) {
        float yawA = entity1.getLocation().getYaw();
        float yawB = entity2.getLocation().getYaw();

        float yawDiff = Math.abs(yawA - yawB);

        yawDiff = yawDiff % 360;
        if (yawDiff > 180) {
            yawDiff = 360 - yawDiff;
        }

        return yawDiff < yawDifference;
    }

    public static Vector2d get2DAngleBetweenVectors(Vector pos1, Vector pos2) {
        double dx = pos2.getX() - pos1.getX();
        double dy = pos2.getY() - pos1.getY();
        double dz = pos2.getZ() - pos1.getZ();

        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length == 0) return new Vector2d(0, 0);

        double nx = dx / length;
        double ny = dy / length;
        double nz = dz / length;

        double yaw = Math.toDegrees(Math.atan2(nx, nz));
        double pitch = Math.toDegrees(Math.atan2(ny, Math.sqrt(nx * nx + nz * nz)));

        return new Vector2d(yaw, pitch).mul(-1, -1);
    }

    public static String getNegativeSpaces(int value) {
        String ns = "";
        for(int i = 0; i < value; i++) {
            ns += "ꌕ";
        }
        return ns;
    }

    public static Location raycastBlockLocation(Location startLocation, int steps, double stepDistance) {
        Location loc = startLocation.clone();
        for(int i = 0; i<=steps; i++) {
            loc = getRelativeLocation(loc, stepDistance, 0, 0);
            if(i==steps) {
                return loc;
            }
            if(!loc.getBlock().isPassable()) {
                return loc;
            }
        }
        return loc;
    }

    public static Vector getDirectionBetweenVectors(Location loc1, Location loc2) {
        return loc1.clone().subtract(loc2).toVector().normalize();
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

    public static Vector getRelativeVector(double forward, double right, double up, float setYaw, float setPitch) {
        double yawRad = Math.toRadians(setYaw);
        double pitchRad = Math.toRadians(setPitch);

        double cosYaw = Math.cos(yawRad);
        double sinYaw = Math.sin(yawRad);
        double cosPitch = Math.cos(pitchRad);
        double sinPitch = Math.sin(pitchRad);

        double fX = -sinYaw * cosPitch;
        double fY = -sinPitch;
        double fZ = cosYaw * cosPitch;

        double rX = cosYaw;
        double rY = 0;
        double rZ = sinYaw;

        double uX = -sinYaw * -sinPitch;
        double uY = cosPitch;
        double uZ = cosYaw * -sinPitch;

        double x = (forward * fX) + (right * rX) + (up * uX);
        double y = (forward * fY) + (right * rY) + (up * uY);
        double z = (forward * fZ) + (right * rZ) + (up * uZ);

        return new Vector(x, y, z);
    }

    public static void spawnParticleCircle(Location origin, double size, double speed, @Nullable Vector2d angle, Particle particle, boolean forced) {
        Location center = origin.clone();
        if (angle != null) {
            center.setYaw((float) angle.x);
            center.setPitch((float) angle.y);
        }
        createCircle(center, center.getDirection(), size, speed, particle, forced);
    }

    public static void spawnParticleCircle(Location origin, double size, double speed, Vector direction, Particle particle, boolean forced) {
        createCircle(origin, direction, size, speed, particle, forced);
    }

    private static void createCircle(Location center, Vector forward, double size, double speed, Particle particle, boolean forced) {
        World world = center.getWorld();
        if (world == null) return;

        forward = forward.clone().normalize();

        Vector worldUp = Math.abs(forward.getY()) > 0.999 ? new Vector(1, 0, 0) : new Vector(0, 1, 0);
        Vector right = forward.clone().crossProduct(worldUp).normalize();
        Vector up = right.clone().crossProduct(forward).normalize();

        for (int degrees = 0; degrees < 360; degrees += 10) {
            double cos = Math.cos(degrees * Math.PI / 180.0);
            double sin = Math.sin(degrees * Math.PI / 180.0);

            Vector point = right.clone().multiply(cos * size).add(up.clone().multiply(sin * size));

            Location particleLoc = center.clone().add(point);
            Vector particleDir = point.clone().normalize().multiply(speed);

            world.spawnParticle(particle, particleLoc, 0,
                    particleDir.getX(), particleDir.getY(), particleDir.getZ(), Math.abs(speed), null, forced);
        }
    }

    public static void attemptDisableShield(Player p, int time) {
        if(p.isBlocking()) {
            p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
            p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
            p.completeUsingActiveItem();
            p.setCooldown(Material.SHIELD, time);
        }
    }
}
