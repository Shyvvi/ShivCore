package fr.stormer3428.shivcore.constants;

import fr.stormer3428.bliss.entity.projectile.BloodBomb;
import fr.stormer3428.bliss.entity.projectile.BloodBombExplosion;
import fr.stormer3428.bliss.entity.projectile.BloodNeedle;
import fr.stormer3428.bliss.entity.projectile.BloodSlash;
import fr.stormer3428.shivcore.entities.AbstractTickingEntity;
import fr.stormer3428.shivcore.entities.TestEntity;
import fr.stormer3428.shivcore.entities.behavior.BehaviorController;
import fr.stormer3428.shivcore.entities.particle.CustomParticle;
import fr.stormer3428.shivcore.entities.particle.ParticleController;
import fr.stormer3428.shivcore.entities.particle.ParticleFrame;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public enum Entities {
    TEST_ENTITY(new TestEntity(EntityType.ARMOR_STAND, "testEntity", new BehaviorController.Builder()
            .addBehavior(new TestEntity.BlazeBehavior())
            .addBehavior(new TestEntity.BreezeBehavior())
            .setDefaultBehavior(new TestEntity.DefaultBehavior())
            .build())
    ),
    // I'll move this to it's own thing later
    BLOOD_NEEDLE(new BloodNeedle(EntityType.ITEM_DISPLAY, "bloodNeedle", new BehaviorController.Builder()
            .addBehavior(new BloodNeedle.StuckBehavior())
            .addBehavior(new BloodNeedle.LaunchedBehavior())
            .setDefaultBehavior(new BloodNeedle.HoverTick())
            .build())
    ),
    BLOOD_BOMB(new BloodBomb(EntityType.ITEM_DISPLAY, "bloodBomb", new BehaviorController.Builder()
            .setDefaultBehavior(new BloodBomb.DefaultBehavior())
            .build())
    ),
    BLOOD_BOMB_EXPLOSION(new BloodBombExplosion(Material.COMMAND_BLOCK, "bloodBombExplosion", new ParticleController.Builder(0)
            .addFrame(new ParticleFrame(14, 3f))
            .addFrame(new ParticleFrame(14, 2f))
            .addFrame(new ParticleFrame(14, 1f))
            .setBrightness(15)
            .setFacesCamera(true)
            .build())
    ),
    BLOOD_SLASH(new BloodSlash(Material.COMMAND_BLOCK, "bloodSlash", new ParticleController.Builder(2)
            .addFrame(new ParticleFrame(15))
            .addFrame(new ParticleFrame(16))
            .addFrame(new ParticleFrame(17))
            .addFrame(new ParticleFrame(18))
            .addFrame(new ParticleFrame(19))
            .setFixedSize(new Vector(1.5, 1.5, 1.5))
            .setBrightness(15)
            .build())
    ),
    BLOOD_SLASH_IMPACT(new CustomParticle(Material.COMMAND_BLOCK, "bloodSlashImpact", new ParticleController.Builder(0)
            .addFrame(new ParticleFrame(14, 1f))
            .addFrame(new ParticleFrame(14, 0.5f))
            .setBrightness(15)
            .setFacesCamera(true)
            .build())
    ),
    ;

    public final AbstractTickingEntity get;
    private Entities(AbstractTickingEntity entity) {
        this.get = entity;
    }
}
