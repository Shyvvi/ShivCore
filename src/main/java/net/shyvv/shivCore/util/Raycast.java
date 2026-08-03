package net.shyvv.shivCore.util;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.joml.Vector2d;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Raycast {
    boolean collidesBlocks;
    boolean collidesEntities;
    boolean piercesEntities;
    int blockPhaseStepAmount;
    double stepDistance;
    double collisionSize;
    int steps;
    Predicate<Block> blockCollideCondition;
    Predicate<Entity> exemptEntityCondition;
    TriConsumer<Location, Entity, Integer> entityCollideEffect;
    BiConsumer<Location, Integer> raycastEndEffect;
    BiConsumer<Location, Integer> stepEffect;
    TriConsumer<Location, Block, Integer> blockCollideEffect;

    private Raycast(Builder builder) {
        this.collidesBlocks = builder.collidesBlocks;
        this.collidesEntities = builder.collidesEntities;
        this.blockPhaseStepAmount = builder.blockPhaseStepAmount;
        this.stepDistance = builder.stepDistance;
        this.collisionSize = builder.collisionSize;
        this.steps = builder.steps;
        this.blockCollideCondition = builder.blockCollideCondition;
        this.exemptEntityCondition = builder.exemptEntityCondition;
        this.blockCollideEffect = builder.blockCollideEffect;
        this.stepEffect = builder.stepEffect;
        this.entityCollideEffect = builder.entityCollideEffect;
        this.piercesEntities = builder.piercesEntities;
        this.raycastEndEffect = builder.raycastEndEffect;
    }

    /**
     * Create a raycast
     * @param startingLoc create a raycast at the location (provide direction values in Location)
     */
    public void raycast(Location startingLoc) {
        boolean collided = false;
        int phaseTracking = 0;
        Location loc = startingLoc;

        for (int x = 0; x < steps; x++) {
            loc = MiscUtil.getRelativeLocation(startingLoc, x * stepDistance, 0, 0);

            if (stepEffect != null) stepEffect.accept(loc, x);

            if (collidesBlocks && blockCollideCondition != null && blockCollideCondition.test(loc.getBlock())) {
                if (blockPhaseStepAmount > 0) {
                    phaseTracking++;
                    if (phaseTracking >= blockPhaseStepAmount) {
                        if (blockCollideEffect != null) blockCollideEffect.accept(loc, loc.getBlock(), x);
                        collided = true;
                        break;
                    }
                } else {
                    if (blockCollideEffect != null) blockCollideEffect.accept(loc, loc.getBlock(), x);
                    collided = true;
                    break;
                }
            }

            if (collidesEntities) {
                Collection<Entity> collidingEntities = loc.getWorld().getNearbyEntities(loc, collisionSize, collisionSize, collisionSize);
                if (exemptEntityCondition != null) collidingEntities.removeIf(exemptEntityCondition);
                if (entityCollideEffect != null) {
                    for (Entity ce : collidingEntities) {
                        entityCollideEffect.accept(loc, ce, x);
                    }
                }
                if (!collidingEntities.isEmpty() && !piercesEntities) {
                    collided = true;
                    break;
                }
            }
        }

        if (!collided && raycastEndEffect != null) {
            raycastEndEffect.accept(loc, Math.max(steps - 1, 0));
        }
    }

    /**
     * Create a raycast between two locations
     * @param loc1 initial location
     * @param loc2 target location
     */
    public void raycast(Location loc1, Location loc2) {
        Vector2d angle = MiscUtil.get2DAngleBetweenVectors(loc1.toVector(), loc2.toVector());
        Location loc = new Location(loc1.getWorld(), loc1.x(), loc1.y(), loc1.z(), (float) angle.x, (float) angle.y);
        raycast(loc);
    }

    public static class Builder {
        boolean collidesBlocks;
        boolean collidesEntities;
        boolean piercesEntities;
        int blockPhaseStepAmount = 0;
        double stepDistance;
        double collisionSize;
        int steps;
        Predicate<Entity> exemptEntityCondition = null;
        Predicate<Block> blockCollideCondition = null;
        TriConsumer<Location, Entity, Integer> entityCollideEffect = null;
        BiConsumer<Location, Integer> stepEffect = null;
        BiConsumer<Location, Integer> raycastEndEffect = null;
        TriConsumer<Location, Block, Integer> blockCollideEffect = null;

        public Builder(int steps, double stepDistance) {
            this.steps = steps;
            this.stepDistance = stepDistance;
        }

        public Builder collidesBlocks(boolean bool) {
            this.collidesBlocks = bool;
            return this;
        }

        public Builder exemptEntityCondition(Predicate<Entity> predicate) {
            this.exemptEntityCondition = predicate;
            return this;
        }

        public Builder collidesEntities(boolean bool) {
            this.collidesEntities = bool;
            return this;
        }

        public Builder piercesEntities(boolean bool) {
            this.piercesEntities = bool;
            return this;
        }

        public Builder setBlockPhaseStepAmount(int blockPhaseStepAmount) {
            this.blockPhaseStepAmount = blockPhaseStepAmount;
            return this;
        }

        public Builder setCollisionSize(double collisionSize) {
            this.collisionSize = collisionSize;
            return this;
        }

        public Builder blockCollideCondition(Predicate<Block> predicate) {
            this.blockCollideCondition = predicate;
            return this;
        }

        public Builder blockCollideEffect(TriConsumer<Location, Block, Integer> consumer) {
            this.blockCollideEffect = consumer;
            return this;
        }

        public Builder stepEffect(BiConsumer<Location, Integer> consumer) {
            this.stepEffect = consumer;
            return this;
        }

        public Builder entityCollideEffect(TriConsumer<Location, Entity, Integer> consumer) {
            this.entityCollideEffect = consumer;
            return this;
        }

        public Builder raycastEndEffect(BiConsumer<Location, Integer> consumer) {
            this.raycastEndEffect = consumer;
            return this;
        }

        public Raycast build() {
            return new Raycast(this);
        }
    }
}