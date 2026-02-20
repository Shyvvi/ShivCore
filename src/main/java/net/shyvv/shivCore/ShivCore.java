package net.shyvv.shivCore;

import net.shyvv.shivCore.constants.Commands;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.ServerUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

public final class ShivCore extends JavaPlugin {
    private static JavaPlugin providingPlugin;
    public static org.slf4j.Logger log;

    public static void initialize(JavaPlugin plugin) {
        providingPlugin = plugin;
        log = LoggerFactory.getLogger(providingPlugin.getClass());

        Commands.initialize();

        EntityUtil.initialize();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    public static JavaPlugin getInstance() {return JavaPlugin.getPlugin(providingPlugin.getClass());}
}
