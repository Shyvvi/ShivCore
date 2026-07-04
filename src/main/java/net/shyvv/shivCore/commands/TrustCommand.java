package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.player.trust.TrustUtil;
import net.shyvv.shivCore.util.EntityManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.TrustManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrustCommand extends SCCommand {
    public TrustCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }
    String[] VALID_ARGS1 = {"add","remove","list","clear"};

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p && p.isOp()) {
            if(args.length >= 1) {
                if (args[1].equals(VALID_ARGS1[0]) || args[1].equals(VALID_ARGS1[1])) {
                    OfflinePlayer opl = Bukkit.getServer().getOfflinePlayer(args[1]);
                    if (opl.hasPlayedBefore()) {
                        boolean b = args[1].equals(VALID_ARGS1[0]) && !args[1].equals(VALID_ARGS1[1]);
                        TrustUtil.setPlayerTrusted(p, opl.getName(), b);
                        sendMsg(p, b ? "Added "+args[1]+" onto your trusted players list." : "Removed "+args[1]+" from your trusted players list.");
                    } else {
                        sendError(p, args[1] + " has not logged on before or does not exist.");
                    }
                } else if(args[1].equals(VALID_ARGS1[2])) {
                    sendMsg(p, "Trusted Players:");
                    List<String> trustedPlayers = TrustUtil.getTrustedPlayers(p);
                    for(String s : trustedPlayers) {
                        sendMsg(p, " ● "+s);
                    }
                } else if(args[1].equals(VALID_ARGS1[3])) {
                    sendMsg(p, "Removed all players on your trusted players list.");
                    List<String> trustedPlayers = TrustUtil.getTrustedPlayers(p);
                    for(String s : trustedPlayers) {
                        TrustUtil.setPlayerTrusted(p, s, false);
                    }
                } else {
                    sendError(sender, "Invalid args");
                }
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
        if(sender instanceof Player p) {
            if (args.length == 1) {
                tabComplete.add(Arrays.toString(VALID_ARGS1));
                return tabComplete;
            } else if (args.length == 2) {
                if (args[1].equals(VALID_ARGS1[0])) {
                    for (OfflinePlayer opl : Bukkit.getOfflinePlayers()) {
                        tabComplete.add(opl.getName());
                    }
                } else if (args[1].equals(VALID_ARGS1[1])) {
                    tabComplete.addAll(TrustUtil.getTrustedPlayers(p));
                }
            }
        }
        return tabComplete;
    }
}
