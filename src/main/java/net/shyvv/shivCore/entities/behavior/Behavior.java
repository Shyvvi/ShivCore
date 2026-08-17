package net.shyvv.shivCore.entities.behavior;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import org.bukkit.entity.Entity;

import java.util.function.Predicate;

public abstract class Behavior<E extends Entity, T extends AbstractTickingEntity<E>> {
    private final String behaviorID;
    private final boolean ignoresBreaks;
    private final Predicate<Entity> activePredicate;
    public Behavior(String behaviorID, boolean ignoresBreaks, Predicate<Entity> activePredicate) {
        this.behaviorID = behaviorID;
        this.ignoresBreaks = ignoresBreaks;
        this.activePredicate = activePredicate;
    }
    public Behavior(String behaviorID, Predicate<Entity> activePredicate) {
        this(behaviorID, false, activePredicate);
    }
    public Behavior() {
        this("defaultBehavior", false, _ -> true);
    }

    public abstract T entityClass();
    public abstract void tick(E e);

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
