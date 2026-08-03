package net.shyvv.shivCore.damage;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.constants.Configs;
import net.shyvv.shivCore.player.trust.TrustUtil;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.MiscUtil;
import net.shyvv.shivCore.util.Raycast;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class DamageType {
    String deathMessage;
    String damageTypeID;
    int ignitionTime;
    boolean ignoresIFrames;
    boolean damagesTrustedPlayers;
    boolean blockable;
    BiConsumer<LivingEntity, LivingEntity> consumer;
    Vector2d knockback;
    private DamageType(DamageType.Builder builder) {
        this.damageTypeID = builder.damageTypeID;
        this.deathMessage = builder.deathMessage;
        this.ignoresIFrames = builder.ignoresIFrames;
        this.damagesTrustedPlayers = builder.damagesTrustedPlayers;
        this.knockback = builder.knockback;
        this.ignitionTime = builder.ignitionTime;
        this.blockable = builder.blockable;
        this.consumer = builder.consumer;
        DamageManager.damageTypeList.add(this);
    }

    public void damage(LivingEntity victim, @Nullable LivingEntity attacker, double amount) {
        // I know that this is a "bad practice" but I didn't wanna put everything into a conditional statement and introduce conflicting variables
        if(victim instanceof Player p && attacker instanceof Player pa && TrustUtil.playerIsTrusted(pa, p.getName()) && !damagesTrustedPlayers) return;

        for(int i = 0; i < DamageManager.damageTypeList.size(); i++) {
            if(damageTypeID.equals(DamageManager.damageTypeList.get(i).damageTypeID)) {
                EntityUtil.setInt(victim, i + 1, "customDamageType");
                break;
            }
        }

        if((knockback.x != 0 || knockback.y != 0) && victim.getNoDamageTicks() <= 0) {
            if(attacker != null) {
                Vector kb = MiscUtil.getDirectionBetweenVectors(victim.getLocation(), attacker.getLocation());
                victim.setVelocity(victim.getVelocity().add(kb.multiply(knockback.x)));
                victim.setVelocity(victim.getVelocity().add(new Vector(0, knockback.y, 0)));
            }
        }
        if(consumer != null) consumer.accept(victim, attacker);

        if(victim instanceof Player pv) {
            if(blockable && pv.isBlocking()) {
                EquipmentSlot shieldSlot = pv.getActiveItemHand();
                pv.damageItemStack(shieldSlot, (int) amount);
            } else {
                if (attacker instanceof Player pa && TrustUtil.playerIsTrusted(pa, pv.getName()) && damagesTrustedPlayers) {
                    pv.damage(Float.parseFloat(Configs.SHIVCORE_CONFIG.getString("trustedPlayersDamageReduction")) * amount);
                }
                pv.damage(amount);
            }
        } else {
            victim.damage(Float.parseFloat(Configs.SHIVCORE_CONFIG.getString("mobDamageMultiplier")) * amount);
        }

        if(ignitionTime > 0) victim.setFireTicks(ignitionTime);
        if(ignoresIFrames) ShivCore.getInstance().getServer().getScheduler().runTaskLater(ShivCore.getInstance(), () -> victim.setNoDamageTicks(0), 1);

        ShivCore.getInstance().getServer().getScheduler().runTaskLater(ShivCore.getInstance(), () -> {
            if (victim.isValid()) {
                EntityUtil.setInt(victim, 0, "customDamageType");
            }
        }, 1);
    }

    public void damageAoERaycasts(@Nullable LivingEntity attacker, Location loc, double amount, int size, double damageFalloff) {
        for(Entity e : loc.getNearbyLivingEntities(size, size, size)) {
            if(attacker != null && e.getUniqueId().equals(attacker.getUniqueId())) continue;
            Raycast rc = new Raycast.Builder(size*10, 0.1)
                    .collidesEntities(true)
                    .collidesBlocks(true)
                    .blockCollideCondition(Block::isCollidable)
                    .setBlockPhaseStepAmount(5)
                    .piercesEntities(true)
                    .entityCollideEffect((_, entity, integer) -> {
                        if (entity instanceof LivingEntity li) {
                            damage(li, attacker, amount - (damageFalloff * integer));
                        }
                    })
                    .exemptEntityCondition(entity -> attacker != null && entity.getUniqueId().equals(attacker.getUniqueId()))
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
        BiConsumer<LivingEntity, LivingEntity> consumer;
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

        public Builder setDamageEffect(BiConsumer<LivingEntity, LivingEntity> consumer) {
            this.consumer = consumer;
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
