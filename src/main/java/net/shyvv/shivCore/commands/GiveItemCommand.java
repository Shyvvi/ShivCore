package net.shyvv.shivCore.commands;

import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.item.ItemManager;
import net.shyvv.shivCore.item.SCItem;
import net.shyvv.shivCore.util.EntityManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GiveItemCommand extends SCCommand {
    public GiveItemCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p && p.isOp()) {
            if(args.length >= 1) {
                for (SCItem sci : ItemManager.CUSTOM_ITEMS) {
                    if (sci.getItemID().equalsIgnoreCase(args[0])) {
                        p.getInventory().addItem(sci.getDefaultItemStack());
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

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws CommandException, IllegalArgumentException {
        List<String> tabComplete = new ArrayList<>();
        if(args.length == 1) {
            for(SCItem ate : ItemManager.CUSTOM_ITEMS) {
                tabComplete.add(ate.getItemID());
            }
            return tabComplete;
        }

        return tabComplete;
    }
}
