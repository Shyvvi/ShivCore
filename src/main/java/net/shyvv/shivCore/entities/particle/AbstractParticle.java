package net.shyvv.shivCore.entities.particle;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractParticle extends AbstractTickingEntity {
    ParticleController particleController;
    Material baseItem;
    int lifeTime;

    public AbstractParticle(Material baseItem, String entityID, ParticleController particleController) {
        super(EntityType.ITEM_DISPLAY, entityID);
        this.particleController = particleController;
        this.baseItem = baseItem;

        this.lifeTime = particleController.getFrameAmount() * (particleController.getFrameDuration()+1);
    }

    public AbstractParticle(Material baseItem, String entityID, int lifeTime, ParticleController particleController) {
        super(EntityType.ITEM_DISPLAY, entityID);
        this.particleController = particleController;
        this.baseItem = baseItem;
        this.lifeTime = lifeTime;
    }

    public abstract void particleTick(Entity e);
    public abstract void particleSpawnArgs(Entity e);

    public ParticleController getParticleController() {return this.particleController;}

    @Override
    public void tick(Entity entity) {
        getParticleController().tickFrames(entity);
        particleTick(entity);

        int lt = incrementInt(entity, "lifeTime");
        if(lt > lifeTime) {
            entity.remove();
        }
    }

    @Override
    protected void spawnArgs(Entity entity) {
        ItemDisplay ie = (ItemDisplay) entity;
        ItemStack displayStack = new ItemStack(baseItem);
        ie.setItemStack(displayStack);
        particleController.init(entity);
        ie.setTeleportDuration(2);
        ie.setInterpolationDuration(1);
        particleSpawnArgs(entity);
    }
}
