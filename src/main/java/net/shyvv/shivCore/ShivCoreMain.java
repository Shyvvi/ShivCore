package net.shyvv.shivCore;

import net.shyvv.shivCore.commands.CommandTabCompleter;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShivCoreMain extends JavaPlugin {
    private static JavaPlugin providingPlugin = null;

    public static void initialize(JavaPlugin plugin) {
        providingPlugin = plugin;
        EntityUtil.initialize();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    public static JavaPlugin getInstance() {return JavaPlugin.getPlugin(providingPlugin.getClass());}

    private void registerCommand(String cmd, CommandExecutor cmdClass) {
        try {
            this.getCommand(cmd).setExecutor(cmdClass);
        } catch(Exception ignored) {}
    }

    public void setCommandAutofill(String cmd, String[] autofill) {
        this.getCommand(cmd).setTabCompleter(new CommandTabCompleter(autofill));
    }
}
