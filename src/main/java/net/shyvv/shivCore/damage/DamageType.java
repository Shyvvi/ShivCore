package net.shyvv.shivCore.damage;

import net.shyvv.shivCore.util.EntityUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.joml.Vector2d;

public class DamageType implements EntityUtils {
    String deathMessage;
    String damageTypeID;
    int ignitionTime;
    boolean ignoresIFrames;
    Vector2d knockback;
    private DamageType(String damageTypeID, String deathMessage, DamageType.Builder builder) {
        this.damageTypeID = damageTypeID;
        this.deathMessage = deathMessage;
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

    public void damageAoERaycasts(Location loc, double amount, double size, double damageFalloff) {
        for(Entity e : loc.getNearbyLivingEntities(size, size, size)) {

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
            return new DamageType(this.damageTypeID, this.deathMessage, this);
        }
    }
}
