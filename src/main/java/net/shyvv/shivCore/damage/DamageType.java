package net.shyvv.shivCore.damage;

import net.shyvv.shivCore.constants.Configs;
import net.shyvv.shivCore.player.trust.TrustUtil;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.Raycast;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector2d;

import javax.annotation.Nullable;

public class DamageType {
    String deathMessage;
    String damageTypeID;
    int ignitionTime;
    boolean ignoresIFrames;
    boolean damagesTrustedPlayers;
    boolean blockable;
    Vector2d knockback;
    private DamageType(DamageType.Builder builder) {
        this.damageTypeID = builder.damageTypeID;
        this.deathMessage = builder.deathMessage;
        this.ignoresIFrames = builder.ignoresIFrames;
        this.damagesTrustedPlayers = builder.damagesTrustedPlayers;
        this.knockback = builder.knockback;
        this.ignitionTime = builder.ignitionTime;
        this.blockable = builder.blockable;
        DamageManager.damageTypeList.add(this);
    }

    public void damage(LivingEntity victim, @Nullable LivingEntity attacker, double amount) {
        for(int i = 0; i<DamageManager.damageTypeList.size(); i++) {
            if(damageTypeID.equals(DamageManager.damageTypeList.get(i).damageTypeID)) {
                EntityUtil.setInt(victim, i, "customDamageType");
                break;
            }
        }
        if(victim instanceof Player pv) {
            if(blockable && pv.isBlocking()) {
                EquipmentSlot shieldSlot = pv.getActiveItemHand();
                pv.damageItemStack(shieldSlot, (int) amount);
            } else {
                if (attacker instanceof Player pa && TrustUtil.playerIsTrusted(pa, pv.getName()) && damagesTrustedPlayers) {
                    pv.damage(Float.parseFloat(Configs.SHIVCORE_CONFIG.getValue("trustedPlayersDamageReduction")) * amount);
                }
                pv.damage(amount);
            }
        } else {
            victim.damage(Float.parseFloat(Configs.SHIVCORE_CONFIG.getValue("mobDamageMultiplier"))*amount);
        }
        if(ignitionTime > 0) victim.setFireTicks(ignitionTime);
    }

    public void damageAoERaycasts(@Nullable LivingEntity attacker, Location loc, double amount, int size, double damageFalloff) {
        for(Entity e : loc.getNearbyLivingEntities(size, size, size)) {
            if(attacker != null && e.getUniqueId().equals(attacker.getUniqueId())) continue;
            Raycast rc = new Raycast.Builder(size, 0.1)
                    .collidesEntities(true)
                    .collidesBlocks(true)
                    .blockCollideCondition(Block::isCollidable)
                    .setBlockPhaseStepAmount(5)
                    .piercesEntities(true)
                    .entityCollideEffect((entity, integer) -> {
                        if (entity instanceof LivingEntity li) {
                            if(blockable && li instanceof Player p && p.isBlocking()) {
                                EquipmentSlot shieldSlot = p.getActiveItemHand();
                                p.damageItemStack(shieldSlot, (int) amount);
                            } else {
                                damage(li, attacker, amount - (damageFalloff * integer));
                            }
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
        boolean damagesTrustedPlayers = true;
        boolean blockable = false;
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

        public Builder ignoresIFrames(boolean bool) {
            this.ignoresIFrames = bool;
            return this;
        }

        public Builder isBlockable(boolean bool) {
            this.blockable = bool;
            return this;
        }

        public Builder damagesTrustedPlayers(boolean bool) {
            this.damagesTrustedPlayers = bool;
            return this;
        }

        public DamageType build() {
            return new DamageType(this);
        }
    }
}
