package net.shyvv.shivCore.config;

import java.util.List;

public class DefaultConfig {
    public String key;
    public String defaultValue;
    public List<String> comments;
    public DefaultConfig(String key, String defaultValue, List<String> comments) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    public boolean initializeDefault(ConfigFile configFile) {
        if(!configFile.stringExists(key)) {
            configFile.setString(key, defaultValue);
            configFile.getConfig().setComments(key, comments);
            return true;
        }
        return false;
    }
}
