package net.shyvv.shivCore.entities;

import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class TemporaryEntity extends AbstractTickingEntity {
    public TemporaryEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        super(baseEntity, entityID, behaviorController);
    }
    public TemporaryEntity(EntityType baseEntity, String entityID) {
        super(baseEntity, entityID);
    }

    /// The amount of time that the projectile exists for
    public abstract int entityLifetime();

    /// The ticking method for projectiles
    public abstract void entityTick(Entity e);

    @Override
    public void tick(Entity entity) {
        entityTick(entity);
        int lt = EntityUtil.incrementInt(entity, "lifeTime");
        if(lt > entityLifetime()) {
            entity.remove();
        }
    }
}
