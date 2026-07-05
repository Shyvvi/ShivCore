package net.shyvv.shivCore.example;

import net.shyvv.shivCore.entities.animation.AnimationController;
import net.shyvv.shivCore.entities.animation.Frame;
import net.shyvv.shivCore.entities.animation.FrameAnimation;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.entities.particle.CustomParticle;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.function.Predicate;

public class Entities {
    public static TestEntity TEST_ENTITY = new TestEntity(EntityType.ARMOR_STAND, "testEntity", new BehaviorController.Builder()
            .addBehavior(new TestEntity.Blaze("blazeBehavior", false, entity -> EntityUtil.getInt(entity, "lifetime") > 400))
            .addBehavior(new TestEntity.Breeze("breezeBehavior", false, entity -> EntityUtil.getInt(entity, "lifetime") > 200))
            .setDefaultBehavior(new TestEntity.Default("defaultBehavior"))
            .build());

    public static TestAnimatedEntity TEST_ANIMATED_ENTITY = new TestAnimatedEntity(Material.COMMAND_BLOCK, "testAnimatedEntity", new AnimationController.Builder()
            .addAnimation(new FrameAnimation.Builder(3, entity -> entity instanceof LivingEntity li && li.getHealth() > 10)
                    .addFrame(new Frame(1, 1))
                    .addFrame(new Frame(2, 2))
                    .addFrame(new Frame(3, 3))
                    .setBrightness(10)
                    .setFacesCamera(true)
                    .build())
            .addAnimation(new FrameAnimation.Builder(3, entity -> entity instanceof LivingEntity li && li.getHealth() > 5)
                    .addFrame(new Frame(4, 4))
                    .addFrame(new Frame(5, 5))
                    .addFrame(new Frame(6, 6))
                    .setBrightness(10)
                    .setFacesCamera(true)
                    .build())
            .addAnimation(new FrameAnimation.Builder(3, e -> true) // default animation as it is at the bottom of the hierarchy
                    .addFrame(new Frame(7, 7))
                    .addFrame(new Frame(8, 8))
                    .addFrame(new Frame(9, 9))
                    .setBrightness(10)
                    .setFacesCamera(true)
                    .build())
            .build()
    );

    public static CustomParticle TEST_PARTICLE = new CustomParticle(Material.COMMAND_BLOCK, "testParticle", new FrameAnimation.Builder(3, e -> true)
            .addFrame(new Frame(1, 4))
            .addFrame(new Frame(2, 5))
            .addFrame(new Frame(3, 6))
            .setBrightness(10)
            .build());
}
