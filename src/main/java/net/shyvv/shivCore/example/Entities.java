package net.shyvv.shivCore.example;

import net.shyvv.shivCore.entities.behavior.BehaviorController;
import org.bukkit.entity.EntityType;

public class Entities {
    public static TestEntity TEST_ENTITY = new TestEntity(EntityType.ARMOR_STAND, "testEntity", new BehaviorController.Builder()
            .addBehavior(new TestEntity.BlazeBehavior())
            .addBehavior(new TestEntity.BreezeBehavior())
            .setDefaultBehavior(new TestEntity.DefaultBehavior())
            .build());
}
