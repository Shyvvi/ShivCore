package fr.stormer3428.shivcore.entities.behavior;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BehaviorController {
    private final List<Behavior> behaviors;
    private final Behavior defaultBehavior;

    BehaviorController(Builder builder) {
        this.defaultBehavior = builder.defaultBehavior;
        this.behaviors = builder.behaviors;
    }

    public void tickBehaviors(Entity e) {
        boolean useDefaultBehavior = true;
        for(Behavior behavior : behaviors) {
            if(behavior.isActive(e)) {
                useDefaultBehavior = false;
                behavior.tick(e);
                if(!behavior.ignoresBreaks()) {
                    break;
                }
            }
        }
        if(useDefaultBehavior) {
            defaultBehavior.tick(e);
        }
    }

    public Behavior getPrimaryActiveBehavior(Entity e) {
        for(Behavior behavior : behaviors) {
            if(behavior.isActive(e)) {
                return behavior;
            }
        }
        return defaultBehavior;
    }

    public Behavior getBehavior(String behaviorID) {
        for(Behavior behavior : behaviors) {
            if(behavior.behaviorID().equals(behaviorID)) {
                return behavior;
            }
        }
        return null;
    }

    public void setActive(Entity e, String behaviorID, boolean active) {
        Behavior behavior = getBehavior(behaviorID);
        behavior.setActive(e, active);
    }

    public static class Builder {
        private final List<Behavior> behaviors = new ArrayList<>();
        private Behavior defaultBehavior;

        public Builder addBehavior(Behavior behavior) {
            behaviors.add(behavior);
            return this;
        }

        public Builder setDefaultBehavior(Behavior behavior) {
            this.defaultBehavior = behavior;
            return this;
        }

        public BehaviorController build() {
            return new BehaviorController(this);
        }
    }
}