package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpawnEntity extends SCCommand implements CommandExecutor {
    public SpawnEntity(boolean b) {
        super(b);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public void register() {
        registerCMD(this, ShivCore.SPAWN_ENTITY_CMD);
    }
}
