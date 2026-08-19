package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.entities.AbstractTickingEntity;
import net.shyvv.shivCore.player.trust.TrustUtil;
import net.shyvv.shivCore.util.EntityManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.TrustManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrustCommand extends SCCommand {
    public TrustCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID, description, usageMessage);
    }

    String[] VALID_ARGS1 = {"add", "remove", "list", "clear"};

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (sender instanceof Player p && p.isOp()) {
            if (args.length >= 1) {
                if (args[0].equals(VALID_ARGS1[0]) || args[0].equals(VALID_ARGS1[1])) {
                    OfflinePlayer opl = Bukkit.getServer().getOfflinePlayer(args[1]);
                    if (opl.hasPlayedBefore()) {
                        boolean b = args[0].equals(VALID_ARGS1[0]) && !args[0].equals(VALID_ARGS1[1]);
                        TrustUtil.setPlayerTrusted(p, opl.getName(), b);
                        sendMsg(p, b ? "Added " + args[1] + " onto your trusted players list." : "Removed " + args[1] + " from your trusted players list.");
                    } else if (opl.getName().equals(p.getName())) {
                        sendError(p, "\"As soon as you trust yourself, you will know how to live.\" - Johann Wolfgang von Goethe");
                    } else {
                        sendError(p, args[1] + " has not logged on before or does not exist.");
                    }
                } else if (args[0].equals(VALID_ARGS1[2])) {
                    List<String> trustedPlayers = TrustUtil.getTrustedPlayers(p);
                    if (trustedPlayers.isEmpty()) {
                        sendError(p, "Your trusted players list is empty.");
                    } else {
                        sendMsg(p, "Trusted Players:");
                        for (String s : trustedPlayers) {
                            sendMsg(p, " ● " + s);
                        }
                    }
                } else if (args[0].equals(VALID_ARGS1[3])) {
                    sendMsg(p, "Removed all players on your trusted players list.");
                    TrustUtil.resetTrustedPlayers(p);
                } else {
                    sendError(sender, "Invalid args");
                }
            } else {
                sendError(sender, "Invalid args");
            }
        } else if (!(sender instanceof Player)) {
            sendError(sender, "You must be a player in order to execute this command.");
        } else {
            sendError(sender, "You do not have permission to use this command.");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws CommandException, IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player p)) {
            return completions;
        }

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(VALID_ARGS1), completions);
        }

        if (args.length == 2) {
            List<String> candidates = new ArrayList<>();

            if (args[0].equalsIgnoreCase(VALID_ARGS1[0])) {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    String name = offlinePlayer.getName();
                    if (name != null) {
                        candidates.add(name);
                    }
                }
            } else if (args[0].equalsIgnoreCase(VALID_ARGS1[1])) {
                candidates.addAll(TrustUtil.getTrustedPlayers(p));
            }

            return StringUtil.copyPartialMatches(args[1], candidates, completions);
        }

        return completions;
    }
}
