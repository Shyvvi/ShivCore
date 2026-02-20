package net.shyvv.shivCore.util;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.commands.CommandTabCompleter;
import org.bukkit.command.Command;

public class ServerUtil {

    public static final String OPEN_UI_CMD = "openui";
    public static final String GIVE_ITEM_CMD = "giveitem";

    public static void registerCommand(Command cmdClass) {
        try {
            ShivCore.getInstance().getServer().getCommandMap().register("shivcore", cmdClass);
            ShivCore.log.info("Registered command {}", cmdClass.getName());
        } catch(Exception e) {
            ShivCore.log.error("Failed to register command {}", cmdClass.getName());
        }
    }
}
