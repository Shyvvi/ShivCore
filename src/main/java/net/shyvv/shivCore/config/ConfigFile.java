package net.shyvv.shivCore.config;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("rawtypes")
public class ConfigFile {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    private final String name;
    List<AbstractDefaultConfig> defaultConfigList;
    public ConfigFile(String name, JavaPlugin plugin, AbstractDefaultConfig... defaultConfigList) {
        this.name = name;
        this.plugin = plugin;
        this.defaultConfigList = new ArrayList<>(List.of(defaultConfigList));
        ConfigManager.CONFIG_FILES.add(this);
    }

    public void initialize() {
        configFile = new File(plugin.getDataFolder(), name+".yml");
        if (!this.getConfigFile().exists()) {
            ShivCore.log.info("Creating config {} as it was not found", name);
        }
        config = YamlConfiguration.loadConfiguration(this.getConfigFile());
        saveConfig();

        for(AbstractDefaultConfig dc : defaultConfigList) {
            if(!config.contains(dc.key)) ShivCore.log.info("Creating defaultConfig - {}", dc.key);
            dc.initializeDefault(this);
        }
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T addDefaultConfig(AbstractDefaultConfig<T> defaultConfig) {
        this.defaultConfigList.add(defaultConfig);
        return defaultConfig.get();
    }

    private File getConfigFile() {
        return configFile;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void setString(String key, String value) {
        config.set(key, value);
    }

    public void setValue(String key, Object value) {
        config.set(key, value);
    }

    public String getValue(String key) {
        return getString(key);
    }

    public boolean stringExists(String key) {
        return config.contains(key);
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public List<String> getStringList(String key) {
        return config.getStringList(key);
    }


}
