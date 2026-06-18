package net.shyvv.shivCore.example;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.entities.behavior.Behavior;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TestEntity extends AbstractTickingEntity {
    public TestEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        super(baseEntity, entityID, behaviorController);
    }

    @Override
    public void tick(Entity entity) {
        BehaviorController behaviors = this.getBehaviorController();
        for (Player p : entity.getWorld().getPlayers()) {
            Material held = p.getEquipment().getItemInMainHand().getType();
            behaviors.setActive(entity, "blazeBehavior", held.equals(Material.BLAZE_ROD));
            behaviors.setActive(entity, "breezeBehavior", held.equals(Material.BREEZE_ROD));

            break;
        }
    }

    @Override
    protected void spawnArgs(Entity entity) {}

    public static class DefaultBehavior extends Behavior {

        @Override
        public void tick(Entity e) {
            e.getWorld().spawnParticle(Particle.CRIT, e.getLocation(), 1);
            MiscUtil.debugMessage("default behavior");
        }

        @Override
        public String behaviorID() {
            return "defaultBehavior";
        }

        @Override
        public boolean ignoresBreaks() {
            return false;
        }

        @Override
        public AbstractTickingEntity entityClass() {
            return null;
        }
    }

    public static class BlazeBehavior extends Behavior {

        @Override
        public void tick(Entity e) {
            e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 1);
            e.getWorld().spawnParticle(Particle.SMOKE, e.getLocation(), 1);
            MiscUtil.debugMessage("fire behavior");
        }

        @Override
        public String behaviorID() {
            return "blazeBehavior";
        }

        @Override
        public boolean ignoresBreaks() {
            return false;
        }

        @Override
        public AbstractTickingEntity entityClass() {
            return null;
        }
    }

    public static class BreezeBehavior extends Behavior {

        @Override
        public void tick(Entity e) {
            e.getWorld().spawnParticle(Particle.GUST, e.getLocation(), 1);
            MiscUtil.debugMessage("air behavior");
        }

        @Override
        public String behaviorID() {
            return "breezeBehavior";
        }

        @Override
        public boolean ignoresBreaks() {
            return false;
        }

        @Override
        public AbstractTickingEntity entityClass() {
            return null;
        }
    }
}
