package net.shyvv.shivCore.entities.behavior;

import net.shyvv.shivCore.entities.AbstractTickingEntity;

public abstract class DefaultBehavior<T extends AbstractTickingEntity> extends Behavior<T> {
    public DefaultBehavior(String behaviorID) {
        super(behaviorID, false, entity -> true);
    }

}
