package net.shyvv.shivCore.entities.behavior;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import org.bukkit.entity.Entity;

import java.util.function.Predicate;

public abstract class Behavior<T extends AbstractTickingEntity> {
    private final String behaviorID;
    private final boolean ignoresBreaks;
    private final Predicate<Entity> activePredicate;
    public Behavior(String behaviorID, boolean ignoresBreaks, Predicate<Entity> activePredicate) {
        this.behaviorID = behaviorID;
        this.ignoresBreaks = ignoresBreaks;
        this.activePredicate = activePredicate;
    }

    public abstract T entityClass();
    public abstract void tick(Entity e);

    public String behaviorID() {
        return this.behaviorID;
    }

    public boolean ignoresBreaks() {
        return this.ignoresBreaks;
    }

    public boolean isActive(Entity e) {
        return activePredicate.test(e);
    }
}
