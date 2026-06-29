package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.item.ItemManager;
import net.shyvv.shivCore.item.SCItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveItemCommand extends SCCommand {
    public GiveItemCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p && p.isOp()) {
            if(args.length >= 1) {
                for (SCItem sci : ItemManager.CUSTOM_ITEMS) {
                    if (sci.getItemID().equals(args[0])) {
                        p.give(sci.getDefaultItemStack());
                        return true;
                    }
                }
                sendError(sender, "Item with ID " + args[0].toLowerCase() + " not found.");
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
}
