package net.shyvv.shivCore.util;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RaycastBuilder {
    boolean collidesBlocks;
    boolean collidesEntities;
    boolean piercesEntities;
    int blockPhaseStepAmount;
    double stepDistance;
    double collisionSize;
    int steps;
    Predicate<Block> blockCollideCondition;
    Predicate<Entity> exemptEntityCondition;
    Consumer<Entity> entityCollideEffect;
    Consumer<Location> stepEffect;
    Consumer<Block> blockCollideEffect;
    private RaycastBuilder(Builder builder) {
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
    }

    /**
     * Create a raycast
     * @param loc create a raycast at the location (provide direction values in Location)
     */
    public void raycast(Location loc) {
        for(int x = 0; x<steps; x++) {
            stepEffect.accept(loc);

            if(collidesBlocks && blockCollideCondition.test(loc.getBlock())) {
                if(blockPhaseStepAmount > 0) {
                    int i = ServerUtil.incrementInt("raycastPhaseTracking");
                    if(i >= blockPhaseStepAmount) {

                    }
                } else {

                }
                break;
            }
            if(collidesEntities) {
                Collection<Entity> collidingEntities = loc.getWorld().getNearbyEntities(loc, collisionSize, collisionSize, collisionSize);
                collidingEntities.removeIf(exemptEntityCondition);
                for (Entity ce : collidingEntities) {
                    entityCollideEffect.accept(ce);
                }
                if (!collidingEntities.isEmpty() && !piercesEntities) {
                    break;
                }
            }
        }
        ServerUtil.delete("raycastPhaseTracking");
    }

    /**
     * Create a raycast between two locations
     * @param loc1 initial location
     * @param loc2 target location
     */
    public void raycast(Location loc1, Location loc2) {

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
        Consumer<Entity> entityCollideEffect = null;
        Consumer<Location> stepEffect = null;
        Consumer<Block> blockCollideEffect = null;
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
         * Whether the raycast collides with blocks or not
         * @param bool boolean
         * @return Builder
         */
        public Builder setCollidesBlocks(boolean bool) {
            this.collidesBlocks = bool;
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
        public Builder blockCollideEffect(Consumer<Block> consumer) {
            this.blockCollideEffect = consumer;
            return this;
        }

        /**
         * Consumer for what happens on each step of the raycast
         * @param consumer this is great for decorating your raycast with things such as particles
         * @return Builder
         */
        public Builder stepEffect(Consumer<Location> consumer) {
            this.stepEffect = consumer;
            return this;
        }

        /**
         * Consume for what happens when an entity collides with the raycast
         * @param consumer entity collide effect
         * @return Builder
         */
        public Builder entityCollideEffect(Consumer<Entity> consumer) {
            this.entityCollideEffect = consumer;
            return this;
        }

        public RaycastBuilder build() {
            return new RaycastBuilder(this);
        }
    }
}
