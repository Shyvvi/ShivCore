package net.shyvv.shivCore.config;

import java.util.List;

public class FloatDefaultConfig extends AbstractDefaultConfig<Float>{
    public FloatDefaultConfig(String key, Float defaultValue, List<String> comments) {
        super(key, defaultValue, comments);
    }
    public FloatDefaultConfig(String key, Float defaultValue) {
        super(key, defaultValue, List.of(""));
    }
}
