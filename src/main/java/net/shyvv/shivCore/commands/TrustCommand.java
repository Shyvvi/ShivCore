package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.util.EntityManager;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrustCommand extends SCCommand {
    public TrustCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }
    String[] VALID_ARGS1 = {"add","remove",""};

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p && p.isOp()) {
            if(args.length >= 1) {
                if(args[1].equals(VALID_ARGS1[0])) {
                    
                }
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
            for(AbstractTickingEntity ate : EntityManager.TICKING_ENTITIES) {
               tabComplete.add(ate.getEntityID().asString());
            }
            return tabComplete;
        }

        return tabComplete;
    }
}
