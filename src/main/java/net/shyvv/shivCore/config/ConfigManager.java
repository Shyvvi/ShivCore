package net.shyvv.shivCore.config;

import net.shyvv.shivCore.ShivCore;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    public static List<ConfigFile> CONFIG_FILES = new ArrayList<>();

    public static ConfigFile ABILITY_CONFIG = new ConfigFile("abilities-config", ShivCore.getInstance());

    public static void initialize() {
        // initialize all the config files
        for(ConfigFile cf : CONFIG_FILES) {
            cf.initialize();
        }
    }
}
