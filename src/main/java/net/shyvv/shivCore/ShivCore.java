package net.shyvv.shivCore;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.shyvv.shivCore.commands.CommandTabCompleter;
import net.shyvv.shivCore.commands.SpawnEntity;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.ServerUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShivCore {
    private static JavaPlugin providingPlugin = null;
    public static final String SPAWN_ENTITY_CMD = "scsummon";

    public void initialize(JavaPlugin plugin) {
        providingPlugin = plugin;
        EntityUtil.initialize();
        ServerUtil.initialize(plugin);
    }

    public static JavaPlugin getInstance() {return JavaPlugin.getPlugin(providingPlugin.getClass());}
    public static ComponentLogger getLogger() {return providingPlugin.getComponentLogger();}
}
