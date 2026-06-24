package net.shyvv.shivCore.entities.particle;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.entities.animation.FrameAnimation;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public abstract class AbstractParticle extends AbstractTickingEntity {
    FrameAnimation particleAnimation;
    Material baseItem;
    int lifeTime;

    public AbstractParticle(Material baseItem, String entityID, FrameAnimation particleController) {
        super(EntityType.ITEM_DISPLAY, entityID);
        this.particleAnimation = particleController;
        this.baseItem = baseItem;

        this.lifeTime = particleController.getFrameAmount() * (particleController.getFrameDuration()+1);
    }

    public AbstractParticle(Material baseItem, String entityID, int lifeTime, FrameAnimation particleController) {
        super(EntityType.ITEM_DISPLAY, entityID);
        this.particleAnimation = particleController;
        this.baseItem = baseItem;
        this.lifeTime = lifeTime;
    }

    public abstract void particleTick(Entity e);
    public abstract void particleSpawnArgs(Entity e);

    public FrameAnimation getParticleAnimation() {return this.particleAnimation;}

    public void setDisplaySize(Entity e, Vector3f size) {
        ItemDisplay ie = (ItemDisplay) e;
        Transformation defaultTransformation = ie.getTransformation();
        ie.setTransformation(new Transformation(
                defaultTransformation.getTranslation(),
                defaultTransformation.getLeftRotation(),
                size,
                defaultTransformation.getRightRotation())
        );
    }

    public void displaceDuplicate(Entity entity) {
        for(Entity e : entity.getNearbyEntities(2, 2, 2)) {
            if(getString(entity, "dupeTarget").equals(getString(e, "dupeTarget"))) {
                entity.teleport(e.getLocation().add(0.0, -0.05, 0.0));
                break;
            }
        }
    }

    @Override
    public void tick(Entity entity) {
        getParticleAnimation().tickFrames(entity);
        particleTick(entity);

        if(isDuplicate(entity)) {
            ItemDisplay ie = (ItemDisplay) entity;
            Transformation dt = ie.getTransformation();
            if(dt.getScale().z > 0) {
                ie.setTransformation(new Transformation(
                        dt.getTranslation(),
                        dt.getLeftRotation(),
                        new Vector3f(dt.getScale().x, dt.getScale().y, -dt.getScale().z),
                        dt.getRightRotation())
                );
            }
            if(hasMoved(entity)) {
                displaceDuplicate(entity);
            }
        }

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
        particleAnimation.init(entity);
        ie.setTeleportDuration(2);
        ie.setInterpolationDuration(1);
        particleSpawnArgs(entity);

    }
}
