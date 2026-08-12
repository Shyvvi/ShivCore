package net.shyvv.shivCore.util;

import net.shyvv.shivCore.entities.RopeSegment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class RopeUtil {
    private static final Map<Entity, List<Entity>> activeRopes = new ConcurrentHashMap<>();

    public static int createRope(Entity ropeBase, Location loc1, Location loc2, double stepDistance, int maxSegments, RopeSegment segment) {
        List<Entity> ropeSegments;
        Vector2d dir = MiscUtil.get2DAngleBetweenVectors(loc1.toVector(), loc2.toVector());
        if (activeRopes.containsKey(ropeBase)) {
            ropeSegments = activeRopes.get(ropeBase);
        } else {
            ropeSegments = new ArrayList<>();
        }
        double distance = loc1.distance(loc2);
        AtomicInteger finalLength = new AtomicInteger();
        int steps = (int) (distance / stepDistance);
        if (steps > maxSegments) steps = maxSegments;
        Raycast r = new Raycast.Builder(steps, stepDistance)
                .stepEffect((l, i) -> {
                    finalLength.getAndIncrement();
                    if(ropeSegments.size() == i) {
                        Entity rope = segment.spawn(l);
                        ropeSegments.add(rope);
                        EntityUtil.setString(rope, ropeBase.getUniqueId().toString(), "ropeAnchoringEntity");
                    } else if(ropeSegments.size() > i) {
                        ropeSegments.get(i).teleport(l);
                        ropeSegments.get(i).setRotation((float) dir.x, (float) dir.y);
                    }
                })
                .raycastEndEffect((l, i) -> {
                    if(i < ropeSegments.size()) {
                        while(i < ropeSegments.size()) {
                            ropeSegments.getLast().remove();
                            ropeSegments.removeLast();
                        }
                    }
                })
                .build();

        activeRopes.put(ropeBase, ropeSegments);
        r.raycast(loc1, loc2);
        return finalLength.get();
    }

    public static  void functionSegments(Entity ropeBase, Consumer<Entity> function) {
        activeRopes.get(ropeBase).forEach(function);
    }

    public static void removeRope(Entity ropeBase) {
        if(activeRopes.containsKey(ropeBase)) {
            activeRopes.get(ropeBase).forEach(Entity::remove);
            activeRopes.remove(ropeBase);
        }
    }
}
