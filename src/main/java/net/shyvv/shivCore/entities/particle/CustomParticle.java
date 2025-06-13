package fr.stormer3428.shivcore.entities.particle;

import fr.stormer3428.shivcore.entities.AbstractTickingEntity;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class CustomParticle extends AbstractParticle {
    public CustomParticle(Material baseItem, String entityID, ParticleController particleController) {
        super(baseItem, entityID, particleController);
    }

    @Override
    public void particleTick(Entity e) {}

    @Override
    public void particleSpawnArgs(Entity e) {}
}
