package net.shyvv.shivCore.example;

import net.shyvv.shivCore.entities.animation.AbstractAnimatedEntity;
import net.shyvv.shivCore.entities.animation.AnimationController;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class TestAnimatedEntity extends AbstractAnimatedEntity {


    public TestAnimatedEntity(Material baseItem, String entityID, AnimationController ac) {
        super(baseItem, entityID, ac);
    }

    @Override
    public void entityTick(Entity e) {

    }

    @Override
    public void entitySpawnArgs(Entity e) {

    }
}
