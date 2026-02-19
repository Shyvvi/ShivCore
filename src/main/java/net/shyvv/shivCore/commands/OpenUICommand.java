package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.ui.InvUserInterface;
import net.shyvv.shivCore.util.managers.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenUICommand implements CommandExecutor, CommandMsgHelper {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(commandSender instanceof Player p && p.isOp()) {
            if(args.length >= 1) {
                for (InvUserInterface iui : GUIManager.USER_INTERFACES) {
                    if (iui.getID().equals(args[0])) {
                        iui.openUI(p);
                        return true;
                    }
                }
                sendError(commandSender, "Interface of " + args[0].toLowerCase() + " not found.");
                return true;
            } else {
                sendError(commandSender, "Invalid args");
            }
        } else if(!(commandSender instanceof Player)) {
            sendError(commandSender, "You must be a player in order to execute this command.");
        } else {
            sendError(commandSender, "You do not have permission to use this command.");
        }
        return true;
    }
}
