package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.item.ItemManager;
import net.shyvv.shivCore.item.SCItem;
import net.shyvv.shivCore.ui.InvUserInterface;
import net.shyvv.shivCore.ui.GUIManager;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class OpenUICommand extends SCCommand {
    public OpenUICommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p && p.isOp()) {
            if(args.length >= 1) {
                for (InvUserInterface iui : GUIManager.USER_INTERFACES) {
                    if (iui.getID().equals(args[0])) {
                        iui.openUI(p);
                        return true;
                    }
                }
                sendError(sender, "Interface of " + args[0].toLowerCase() + " not found.");
                return true;
            } else {
                sendError(sender, "Invalid args");
            }
        } else if(!(sender instanceof Player)) {
            sendError(sender, "You must be a player in order to execute this command.");
        } else {
            sendError(sender, "You do not have permission to use this command.");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws CommandException, IllegalArgumentException {
        List<String> tabComplete = new ArrayList<>();
        if(args.length == 1) {
            for(InvUserInterface ate : GUIManager.USER_INTERFACES) {
                tabComplete.add(ate.getID());
            }
            return tabComplete;
        }

        return tabComplete;
    }
}
