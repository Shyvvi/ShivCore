package net.shyvv.shivCore.damage;

import net.shyvv.shivCore.util.EntityUtils;
import net.shyvv.shivCore.util.Raycast;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.joml.Vector2d;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class DamageType implements EntityUtils {
    String deathMessage;
    String damageTypeID;
    int ignitionTime;
    boolean ignoresIFrames;
    Vector2d knockback;
    private DamageType(DamageType.Builder builder) {
        this.damageTypeID = builder.damageTypeID;
        this.deathMessage = builder.deathMessage;
        this.ignoresIFrames = builder.ignoresIFrames;
        this.knockback = builder.knockback;
        this.ignitionTime = builder.ignitionTime;
        DamageManager.damageTypeList.add(this);
    }

    public void damage(LivingEntity victim, double amount) {
        for(int i = 0; i<DamageManager.damageTypeList.size(); i++) {
            if(damageTypeID.equals(DamageManager.damageTypeList.get(i).damageTypeID)) {
                setInt(victim, i, "customDamageType");
                break;
            }
        }
        victim.damage(amount);
    }

    public void damageAoERaycasts(Location loc, double amount, int size, double damageFalloff) {
        for(Entity e : loc.getNearbyLivingEntities(size, size, size)) {
            Raycast rc = new Raycast.Builder(size, 0.1)
                    .collidesEntities(true)
                    .collidesBlocks(true)
                    .blockCollideCondition(Block::isCollidable)
                    .setBlockPhaseStepAmount(5)
                    .entityCollideEffect((entity, integer) -> {
                        if (entity instanceof LivingEntity li) {
                            damage(li, amount - (damageFalloff * integer));
                        }
                    })
                    .build();
            if(e instanceof LivingEntity li) {
                rc.raycast(loc, li.getLocation());
                rc.raycast(loc, li.getEyeLocation());
            }
        }
    }

    public String getDeathMessage() {
        return this.deathMessage;
    }

    public String getDamageTypeID() {
        return damageTypeID;
    }

    public static class Builder {
        int ignitionTime = 0;
        boolean ignoresIFrames = false;
        Vector2d knockback = new Vector2d(0,0);
        String damageTypeID;
        String deathMessage;
        public Builder(String damageTypeID, String deathMessage) {
            this.damageTypeID = damageTypeID;
            this.deathMessage = deathMessage;
        }

        public Builder setKnockback(Vector2d knockback) {
            this.knockback = knockback;
            return this;
        }

        public Builder setIgnitionTime(int ticks) {
            this.ignitionTime = ticks;
            return this;
        }

        public Builder setIgnoresIFrames(boolean bool) {
            this.ignoresIFrames = bool;
            return this;
        }

        public DamageType build() {
            return new DamageType(this);
        }
    }
}
