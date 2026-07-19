package net.shyvv.shivCore;

import net.shyvv.shivCore.config.ConfigManager;
import net.shyvv.shivCore.constants.Commands;
import net.shyvv.shivCore.constants.Configs;
import net.shyvv.shivCore.damage.DamageManager;
import net.shyvv.shivCore.item.ItemManager;
import net.shyvv.shivCore.ui.GUIManager;
import net.shyvv.shivCore.util.EntityManager;
import net.shyvv.shivCore.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

public final class ShivCore extends JavaPlugin {
    private static JavaPlugin providingPlugin;
    public static org.slf4j.Logger log;

    public static void initialize(JavaPlugin plugin) {
        providingPlugin = plugin;
        log = LoggerFactory.getLogger(providingPlugin.getClass());
        Configs.initialize();
        ConfigManager.initialize();
        GUIManager.initialize();
        ItemManager.initialize();
        PlayerManager.initialize();
        DamageManager.initialize();
        EntityManager.initialize();
        Commands.initialize();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    public static JavaPlugin getInstance() {return JavaPlugin.getPlugin(providingPlugin.getClass());}
}
