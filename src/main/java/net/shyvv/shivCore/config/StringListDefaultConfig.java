package net.shyvv.shivCore.config;

import java.util.List;

public class StringListDefaultConfig extends AbstractDefaultConfig<List<String>>{
    public StringListDefaultConfig(String key, List<String> defaultValue, List<String> comments) {
        super(key, defaultValue, comments);
    }
    public StringListDefaultConfig(String key, List<String> defaultValue) {
        super(key, defaultValue, List.of(""));
    }
}
