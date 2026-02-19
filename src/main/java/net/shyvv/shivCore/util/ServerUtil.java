package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.commands.CommandTabCompleter;
import net.shyvv.shivCore.commands.SCCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ServerUtil {
    public static List<SCCommand> COMMANDS = new ArrayList<>();

    public static void initialize(JavaPlugin plugin) {
        commands(plugin);
    }

    private static void commands(JavaPlugin plugin) {
        for(SCCommand e : COMMANDS) {
            registerCommand(e.getCommandName(), e.getCommandExecutor(), plugin);
            if (e.hasAutofill()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> setCommandAutofill(e.getCommandName(), e.getAutoFill(), plugin), 20);
            }
        }
    }

    public static void registerCommand(String cmd, CommandExecutor cmdClass, JavaPlugin plugin) {
        try {
            plugin.getCommand(cmd).setExecutor(cmdClass);
            ShivCore.getLogger().info("Registered command {}", cmd);
        } catch(Exception ignored) {
            ShivCore.getLogger().error("Failed to register command {}", cmd);
        }
    }

    public static void setCommandAutofill(String cmd, String[] autofill, JavaPlugin plugin) {
        try {
            plugin.getCommand(cmd).setTabCompleter(new CommandTabCompleter(autofill));
            ShivCore.getLogger().info("Registered autofill for command {}", cmd);
        } catch(Exception ignored) {
            ShivCore.getLogger().error("Failed to register command autofill {}", cmd);
        }
    }
}
