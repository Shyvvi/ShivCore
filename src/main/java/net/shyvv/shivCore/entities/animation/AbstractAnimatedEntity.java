package net.shyvv.shivCore.entities.animation;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class AbstractAnimatedEntity extends AbstractTickingEntity {

    public AbstractAnimatedEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        super(baseEntity, entityID, behaviorController);
    }
    public AbstractAnimatedEntity(EntityType baseEntity, String entityID) {
        super(baseEntity, entityID);
    }

    @Override
    public void tick(Entity entity) {

    }

    @Override
    protected void spawnArgs(Entity entity) {

    }
}
