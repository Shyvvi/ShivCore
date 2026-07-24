package net.shyvv.shivCore.config;

import java.util.List;

public class BooleanDefaultConfig extends AbstractDefaultConfig<Boolean>{
    public BooleanDefaultConfig(String key, boolean defaultValue, List<String> comments) {
        super(key, defaultValue, comments);
    }
    public BooleanDefaultConfig(String key, boolean defaultValue) {
        super(key, defaultValue, List.of(""));
    }
}
