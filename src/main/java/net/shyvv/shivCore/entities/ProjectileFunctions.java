package net.shyvv.shivCore.entities;

import net.shyvv.shivCore.util.MiscUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;

@Deprecated
public interface ProjectileFunctions {

    /// collide Block/Entity is pretty self-explanatory
    void collideBlock(Entity e);
    void collideEntity(Entity projectile, Entity victim);

    /// Called in the for-loop when the entity moves
    void raycastArgs(Entity e);

    /// Whether the projectile collides with the block is based off this boolean
    boolean collidesWithBlock(Block block);

    /// Whether the entity should be excepted from collision or not
    boolean entityCollisionIsExempt(Entity e);

    /// Distance is the distance per-step, steps is how many times the entity
    default Location raycast(Entity e, double distance, int steps, double collisionSize, boolean shouldBreak) {
        for(int z = 0; z<steps; z++) {
            raycastArgs(e);
            mv(e, distance);

            if(collidesWithBlock(e.getLocation().getBlock())) {
                collideBlock(e);
                break;
            }
            Collection<Entity> collidingEntities = e.getWorld().getNearbyEntities(e.getLocation(), collisionSize, collisionSize, collisionSize);
            collidingEntities.remove(e);
            collidingEntities.removeIf(this::entityCollisionIsExempt);
            for(Entity ce : collidingEntities) {
                collideEntity(e, ce);
            }
            if (!collidingEntities.isEmpty() && shouldBreak) {
                break;
            }
        }
        return e.getLocation();
    }

    default Location raycast(Entity e, double distance, int steps, double collisionSize) {
        return raycast(e, distance, steps, collisionSize, false);
    }

    default void mv(Entity entity, double distance) {
        entity.teleport(MiscUtil.getRelativeLocation(entity.getLocation(), distance, 0, 0));
    }

    default void mv(Entity entity, Location startPos, double distance) {
        entity.teleport(MiscUtil.getRelativeLocation(startPos, distance, 0, 0));
    }
}
