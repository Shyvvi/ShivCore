package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.damage.DamageManager;
import net.shyvv.shivCore.damage.DamageType;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomDamageCommand extends SCCommand{
    public CustomDamageCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p) {
            if(DamageManager.damageTypeIDList.contains(args[0])) {
                for(DamageType d : DamageManager.damageTypeList) {
                    if(d.getDamageTypeID().equals(args[0])) {
                        d.damage(p, Double.parseDouble(args[1]));
                        break;
                    }
                }
            } else {
                sendError(sender, "Invalid arguments.");
            }
        } else {
            sendError(sender, "You do not have permission to use this command.");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws CommandException, IllegalArgumentException {
        List<String> tabComplete = new ArrayList<>();
        if(args.length == 1) {
            tabComplete.addAll(DamageManager.damageTypeIDList);
            return tabComplete;
        }

        return tabComplete;
    }
}
