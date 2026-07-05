package net.shyvv.shivCore.constants;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.config.ConfigFile;
import net.shyvv.shivCore.config.AbstractDefaultConfig;
import net.shyvv.shivCore.config.FloatDefaultConfig;
import net.shyvv.shivCore.config.IntDefaultConfig;

import java.util.List;

public class Configs {
    public static final ConfigFile SHIVCORE_CONFIG = new ConfigFile("shivcore", ShivCore.getInstance(),
            new FloatDefaultConfig("trustedPlayersDamageReduction", 0.5f, List.of("The percentage of reduced damage trusted players take.")),
            new FloatDefaultConfig("mobDamageMultiplier", 0.5f, List.of("The percentage of damage which is dealt to mobs."))
    );

    public static void initialize() {}
}
