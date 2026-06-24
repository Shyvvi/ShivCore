package net.shyvv.shivCore.example;

import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.entity.EntityType;

public class Entities {
    public static TestEntity TEST_ENTITY = new TestEntity(EntityType.ARMOR_STAND, "testEntity", new BehaviorController.Builder()
            .addBehavior(new TestEntity.Blaze("blazeBehavior", false, entity -> EntityUtil.getInt(entity, "lifetime") > 400))
            .addBehavior(new TestEntity.Breeze("breezeBehavior", false, entity -> EntityUtil.getInt(entity, "lifetime") > 200))
            .setDefaultBehavior(new TestEntity.Default("defaultBehavior"))
            .build());
}
