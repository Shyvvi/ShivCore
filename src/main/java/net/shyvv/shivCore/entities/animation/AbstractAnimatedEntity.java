package net.shyvv.shivCore.entities.animation;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;

public abstract class AbstractAnimatedEntity extends AbstractTickingEntity<ItemDisplay> {
    AnimationController animationController;
    Material baseItem;
    public AbstractAnimatedEntity(Material baseItem, String entityID, BehaviorController behaviorController, AnimationController ac) {
        super(EntityType.ITEM_DISPLAY, entityID, behaviorController);
        this.baseItem = baseItem;
        this.animationController = ac;
    }
    public AbstractAnimatedEntity(Material baseItem, String entityID, AnimationController ac) {
        this(baseItem, entityID, null, ac);
    }

    @Override
    public void tick(ItemDisplay entity) {
        animationController.tickAnimations(entity);
    }

    @Override
    protected void spawnArgs(ItemDisplay entity) {
        ItemDisplay ie = (ItemDisplay) entity;
        ItemStack displayStack = new ItemStack(baseItem);
        ie.setItemStack(displayStack);
        for(FrameAnimation fa : animationController.animations) fa.init(entity);
        ie.setTeleportDuration(2);
        ie.setInterpolationDuration(1);
    }

    public abstract void entityTick(Entity e);
    public abstract void entitySpawnArgs(Entity e);
}
