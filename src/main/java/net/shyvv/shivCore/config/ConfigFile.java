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
    private final List<AbstractDefaultConfig> defaultConfigList;

    public ConfigFile(String name, JavaPlugin plugin, AbstractDefaultConfig... defaultConfigList) {
        this.name = name;
        this.plugin = plugin;
        this.defaultConfigList = new ArrayList<>(List.of(defaultConfigList));
        this.ensureLoaded();
        ConfigManager.CONFIG_FILES.add(this);
    }

    private synchronized void ensureLoaded() {
        if (this.config != null && this.configFile != null) {
            return;
        }
        File dataFolder = this.plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.configFile = new File(dataFolder, this.name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void initialize() {
        this.ensureLoaded();

        if (!this.configFile.exists()) {
            ShivCore.log.info("Creating config {} as it was not found", this.name);
        }

        for (AbstractDefaultConfig dc : this.defaultConfigList) {
            if (!this.config.contains(dc.key)) {
                ShivCore.log.info("Creating defaultConfig - {}", dc.key);
            }
            dc.initializeDefault(this);
        }
        this.saveConfig();
    }

    public void saveConfig() {
        this.ensureLoaded();
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T addDefaultConfig(AbstractDefaultConfig<T> defaultConfig) {
        if (this.config == null) {
            this.ensureLoaded();
            if (this.config == null) {
                return defaultConfig.get();
            }
        }

        if (!this.config.contains(defaultConfig.key)) {
            this.config.set(defaultConfig.key, defaultConfig.get());
            if (defaultConfig.comments != null && !defaultConfig.comments.isEmpty()) {
                this.config.setComments(defaultConfig.key, defaultConfig.comments);
            }
            if (!this.defaultConfigList.contains(defaultConfig)) {
                this.defaultConfigList.add(defaultConfig);
            }
            this.saveConfig();
            return defaultConfig.get();
        }

        Object fileValue = this.config.get(defaultConfig.key);
        T defaultValue = defaultConfig.get();

        if (fileValue instanceof Number fileNum && defaultValue instanceof Number) {
            String typeName = defaultValue.getClass().getSimpleName();
            Object converted = switch (typeName) {
                case "Integer" -> fileNum.intValue();
                case "Double" -> fileNum.doubleValue();
                case "Float" -> fileNum.floatValue();
                case "Long" -> fileNum.longValue();
                case "Byte" -> fileNum.byteValue();
                case "Short" -> fileNum.shortValue();
                default -> fileValue;
            };
            return (T) converted;
        }

        Class<T> type = (Class<T>) defaultValue.getClass();
        return this.config.getObject(defaultConfig.key, type, defaultValue);
    }

    public File getConfigFile() {
        this.ensureLoaded();
        return this.configFile;
    }

    public FileConfiguration getConfig() {
        this.ensureLoaded();
        return this.config;
    }

    public void setString(String key, String value) {
        this.ensureLoaded();
        this.config.set(key, value);
    }

    public void setValue(String key, Object value) {
        this.ensureLoaded();
        this.config.set(key, value);
    }

    public Object getValue(String key) {
        this.ensureLoaded();
        return this.config.get(key);
    }

    public boolean hasValue(String key) {
        this.ensureLoaded();
        return this.config.contains(key);
    }

    public int getInt(String key) {
        this.ensureLoaded();
        return this.config.getInt(key);
    }

    public String getString(String key) {
        this.ensureLoaded();
        return this.config.getString(key);
    }

    public List<String> getStringList(String key) {
        this.ensureLoaded();
        return this.config.getStringList(key);
    }
}


