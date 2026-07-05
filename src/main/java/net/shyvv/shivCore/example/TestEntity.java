package net.shyvv.shivCore.example;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.entities.behavior.Behavior;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.entities.behavior.DefaultBehavior;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class TestEntity extends AbstractTickingEntity {
    public TestEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        super(baseEntity, entityID, behaviorController);
    }

    @Override
    public void tick(Entity entity) {
        BehaviorController behaviors = this.getBehaviorController();
        EntityUtil.incrementInt(entity, "lifetime");
    }

    @Override
    protected void spawnArgs(Entity entity) {}

    public static class Default extends DefaultBehavior {
        public Default(String behaviorID) {
            super(behaviorID);
        }

        @Override
        public TestEntity entityClass() {
            return Entities.TEST_ENTITY;
        }

        @Override
        public void tick(Entity e) {
            e.getWorld().spawnParticle(Particle.CRIT, e.getLocation(), 1);
            MiscUtil.debugMessage("default behavior");
        }
    }

    public static class Blaze extends Behavior<TestEntity> {
        public Blaze(String behaviorID, boolean ignoresBreaks, Predicate<Entity> activePredicate) {
            super(behaviorID, ignoresBreaks, activePredicate);
        }

        @Override
        public TestEntity entityClass() {
            return Entities.TEST_ENTITY;
        }

        @Override
        public void tick(Entity e) {
            e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 1);
            e.getWorld().spawnParticle(Particle.SMOKE, e.getLocation(), 1);
            MiscUtil.debugMessage("fire behavior");
        }

    }

    public static class Breeze extends Behavior<TestEntity> {
        public Breeze(String behaviorID, boolean ignoresBreaks, Predicate<Entity> activePredicate) {
            super(behaviorID, ignoresBreaks, activePredicate);
        }

        @Override
        public TestEntity entityClass() {
            return Entities.TEST_ENTITY;
        }

        @Override
        public void tick(Entity e) {
            e.getWorld().spawnParticle(Particle.GUST, e.getLocation(), 1);
            MiscUtil.debugMessage("air behavior");
        }
    }
}
