package net.shyvv.shivCore.util;

import org.bukkit.Location;
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
    BiConsumer<Entity, Integer> entityCollideEffect;
    BiConsumer<Location, Integer> raycastEndEffect;
    BiConsumer<Location, Integer> stepEffect;
    BiConsumer<Block, Integer> blockCollideEffect;
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
        for(int x = 0; x<steps; x++) {
            Location loc = MiscUtil.getRelativeLocation(startingLoc, steps*stepDistance, 0, 0);
            ServerUtil.setBoolean("raycastCollided", false);
            stepEffect.accept(loc, x);
            if(collidesBlocks && blockCollideCondition != null && blockCollideCondition.test(loc.getBlock())) {
                if(blockPhaseStepAmount > 0) {
                    int i = ServerUtil.incrementInt("raycastPhaseTracking");
                    if(i >= blockPhaseStepAmount) {
                        if(blockCollideEffect != null) blockCollideEffect.accept(loc.getBlock(), x);
                        ServerUtil.setBoolean("raycasCollided", true);
                        break;
                    }
                } else {
                    if(blockCollideEffect != null) blockCollideEffect.accept(loc.getBlock(), x);
                    ServerUtil.setBoolean("raycasCollided", true);
                    break;
                }
            }

            if(collidesEntities) {
                Collection<Entity> collidingEntities = loc.getWorld().getNearbyEntities(loc, collisionSize, collisionSize, collisionSize);
                if(exemptEntityCondition != null) collidingEntities.removeIf(exemptEntityCondition);
                if(entityCollideEffect != null) {
                    for (Entity ce : collidingEntities) {
                        entityCollideEffect.accept(ce, x);
                    }
                }
                if (!collidingEntities.isEmpty() && !piercesEntities) {
                    ServerUtil.setBoolean("raycasCollided", true);
                    break;
                }
            }

            if(x==steps && raycastEndEffect != null) raycastEndEffect.accept(loc, x);
        }
        ServerUtil.deleteBoolean("raycastCollided");
        ServerUtil.deleteInteger("raycastPhaseTracking");
    }

    /**
     * Create a raycast between two locations
     * @param loc1 initial location
     * @param loc2 target location
     */
    public void raycast(Location loc1, Location loc2) {
        Vector2d angle = MiscUtil.get2DAngleBetweenVectors(loc1.toVector(), loc2.toVector());
        Location loc = new Location(loc1.getWorld(), loc1.x(), loc1.y(), loc1.z(), (float)angle.x, (float)angle.y);
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
        BiConsumer<Entity, Integer> entityCollideEffect = null;
        BiConsumer<Location, Integer> stepEffect = null;
        BiConsumer<Location, Integer> raycastEndEffect = null;
        BiConsumer<Block, Integer> blockCollideEffect = null;
        public Builder(int steps, double stepDistance) {
            this.steps = steps;
            this.stepDistance = stepDistance;
        }

        /**
         * Whether the raycast collides with blocks
         * @param bool boolean
         * @return Builder
         */
        public Builder collidesBlocks(boolean bool) {
            this.collidesBlocks = bool;
            return this;
        }

        /**
         * Predicate which exempts an entity from collision
         * @param predicate Predicate which should include the raycast owner entity
         * @return Builder
         */
        public Builder exemptEntityCondition(Predicate<Entity> predicate) {
            this.exemptEntityCondition = predicate;
            return this;
        }

        /**
         * Whether the raycast collides with entities or not. If you are not checking for entities, keep this off for the sake of optimization.
         * @param bool boolean
         * @return Builder
         */
        public Builder collidesEntities(boolean bool) {
            this.collidesEntities = bool;
            return this;
        }

        /**
         * Whether the raycast pierces entities or not. If false, the raycast will end after colliding with the first entity it encounters
         * @param bool boolean
         * @return Builder
         */
        public Builder piercesEntities(boolean bool) {
            this.piercesEntities = bool;
            return this;
        }

        /**
         * The amount of steps a raycast can ignore the block collision predicate for
         * @param blockPhaseStepAmount number of steps collision will be ignored
         * @return Builder
         */
        public Builder setBlockPhaseStepAmount(int blockPhaseStepAmount) {
            this.blockPhaseStepAmount = blockPhaseStepAmount;
            return this;
        }

        /**
         * The size around a raycast in which an entity can be detected
         * @param collisionSize the "thickness" of the raycast
         * @return Builder
         */
        public Builder setCollisionSize(double collisionSize) {
            this.collisionSize = collisionSize;
            return this;
        }

        /**
         * Predicate which determines whether a block will initiate collision or not
         * @param predicate conditions which will trigger a collision
         * @return Builder
         */
        public Builder blockCollideCondition(Predicate<Block> predicate) {
            this.blockCollideCondition = predicate;
            return this;
        }

        /**
         * Consumer for what happens when the raycast initiates block collision
         * @param consumer block collide effect
         * @return Builder
         */
        public Builder blockCollideEffect(BiConsumer<Block, Integer> consumer) {
            this.blockCollideEffect = consumer;
            return this;
        }

        /**
         * Consumer for what happens on each step of the raycast
         * @param consumer this is great for decorating your raycast with things such as particles
         * @return Builder
         */
        public Builder stepEffect(BiConsumer<Location, Integer> consumer) {
            this.stepEffect = consumer;
            return this;
        }

        /**
         * Consumer for what happens when an entity collides with the raycast
         * @param consumer entity collide effect
         * @return Builder
         */
        public Builder entityCollideEffect(BiConsumer<Entity, Integer> consumer) {
            this.entityCollideEffect = consumer;
            return this;
        }

        /**
         * Consumer for what happens when a raycast ends without colliding with a block or entity
         * @param consumer raycast end effect
         * @return Builder
         */
        public Builder raycastEndEffect(BiConsumer<Location, Integer> consumer) {
            this.raycastEndEffect = consumer;
            return this;
        }

        public Raycast build() {
            return new Raycast(this);
        }
    }
}
