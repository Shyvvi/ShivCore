package fr.stormer3428.shivcore;

import fr.stormer3428.bliss.Main;
import fr.stormer3428.shivcore.commands.CommandTabCompleter;
import fr.stormer3428.shivcore.constants.Entities;
import fr.stormer3428.shivcore.util.EntityUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class ShivCoreMain {
    /// ONLY THING FORKING FROM THE BLISS PLUGIN
    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }

    /// THE OTHER ONLY THING FORKED FROM THE BLISS PLUGIN
    public static void onEnable() {
        EntityUtil.initialize();
    }

    private void registerCommand(String cmd, CommandExecutor cmdClass) {
        try {
            getInstance().getCommand(cmd).setExecutor(cmdClass);
        } catch(Exception ignored) {}
    }

    public void setCommandAutofill(String cmd, String[] autofill) {
        getInstance().getCommand(cmd).setTabCompleter(new CommandTabCompleter(autofill));
    }
}
