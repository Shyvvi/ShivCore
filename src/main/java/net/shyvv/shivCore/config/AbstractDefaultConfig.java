package net.shyvv.shivCore.config;

import java.util.List;

public abstract class AbstractDefaultConfig<T> {
    public String key;
    public T defaultValue;
    public List<String> comments;
    public AbstractDefaultConfig(String key, T defaultValue, List<String> comments) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }
    public AbstractDefaultConfig(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = List.of("");
    }

    public boolean initializeDefault(ConfigFile configFile) {
        if(!configFile.hasValue(key)) {
            configFile.setValue(key, defaultValue);
            configFile.getConfig().setComments(key, comments);
            return true;
        }
        return false;
    }

    /**
     * returns the default value for the default config, use in tandem with ConfigFile.addDefaultValue
     * @return
     */
    public T get() {
        return this.defaultValue;
    }
}
