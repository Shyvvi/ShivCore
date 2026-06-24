package net.shyvv.shivCore.damage;

import net.shyvv.shivCore.ShivCore;

import java.util.ArrayList;
import java.util.List;

public class DamageManager {
    public static List<DamageType> damageTypeList = new ArrayList<>();
    public static List<String> damageTypeIDList = new ArrayList<>();

    static {
        damageTypeList.add(new DamageType.Builder("empty", "died").build());
    }

    public static void initialize() {
        for(DamageType d : damageTypeList) damageTypeIDList.add(d.damageTypeID);
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(new DamageListener(), ShivCore.getInstance());
    }
}
