package net.shyvv.shivCore.entities.particle;

import net.shyvv.shivCore.entities.animation.FrameAnimation;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class CustomParticle extends AbstractParticle {
    public CustomParticle(Material baseItem, String entityID, FrameAnimation particleController) {
        super(baseItem, entityID, particleController);
    }

    @Override
    public void particleTick(Entity e) {}

    @Override
    public void particleSpawnArgs(Entity e) {}
}
