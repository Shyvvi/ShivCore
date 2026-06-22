package net.shyvv.shivCore.damage;

import net.shyvv.shivCore.util.EntityUtils;
import org.bukkit.entity.LivingEntity;

public class DamageType implements EntityUtils {
    String deathMessage;
    String damageTypeID;
    boolean ignoresIFrames = false;
    public DamageType(String damageTypeID, String deathMessage) {
        this.damageTypeID = damageTypeID;
        this.deathMessage = deathMessage;
        DamageManager.damageTypeList.add(this);
    }

    public DamageType(String damageTypeID, String deathMessage, boolean ignoresIFrames) {
        this(damageTypeID, deathMessage);
        this.ignoresIFrames = ignoresIFrames;
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

    public String getDeathMessage() {
        return this.deathMessage;
    }

    public String getDamageTypeID() {
        return damageTypeID;
    }
}
