package net.shyvv.shivCore;

import net.shyvv.shivCore.commands.CommandTabCompleter;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShivCoreMain extends JavaPlugin {
    @Override
    public void onEnable() {
        EntityUtil.initialize();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ShivCoreMain getInstance() {return JavaPlugin.getPlugin(ShivCoreMain.class);}

    private void registerCommand(String cmd, CommandExecutor cmdClass) {
        try {
            this.getCommand(cmd).setExecutor(cmdClass);
        } catch(Exception ignored) {}
    }

    public void setCommandAutofill(String cmd, String[] autofill) {
        this.getCommand(cmd).setTabCompleter(new CommandTabCompleter(autofill));
    }
}
