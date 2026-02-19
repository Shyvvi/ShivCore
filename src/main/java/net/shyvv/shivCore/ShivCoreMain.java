package net.shyvv.shivCore;

import net.shyvv.shivCore.commands.CommandTabCompleter;
import net.shyvv.shivCore.commands.OpenUICommand;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

public final class ShivCoreMain extends JavaPlugin {
    private static JavaPlugin providingPlugin;
    public static org.slf4j.Logger log;
    public static void initialize(JavaPlugin plugin) {
        providingPlugin = plugin;
        log = LoggerFactory.getLogger(providingPlugin.getClass());
        EntityUtil.initialize();
    }

    public static final String OPEN_UI_CMD = "openui";

    @Override
    public void onEnable() {
        registerCommand(OPEN_UI_CMD, new OpenUICommand());
    }

    @Override
    public void onDisable() {}

    public static JavaPlugin getInstance() {return JavaPlugin.getPlugin(providingPlugin.getClass());}

    public static void registerCommand(String cmd, CommandExecutor cmdClass) {
        try {
            getInstance().getCommand(cmd).setExecutor(cmdClass);
        } catch(Exception ignored) {
            log.error("Failed to register command {}", cmd);
        }
    }

    public static void setCommandAutofill(String cmd, String[] autofill) {
        getInstance().getCommand(cmd).setTabCompleter(new CommandTabCompleter(autofill));
    }
}
