package net.shyvv.shivCore.config;

import java.util.List;

public class StringDefaultConfig extends AbstractDefaultConfig<String>{
    public StringDefaultConfig(String key, String defaultValue, List<String> comments) {
        super(key, defaultValue, comments);
    }
    public StringDefaultConfig(String key, String defaultValue) {
        super(key, defaultValue, List.of(""));
    }
}
