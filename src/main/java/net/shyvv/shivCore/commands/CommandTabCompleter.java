package fr.stormer3428.shivcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    private final String[] SUBCOMMANDS;
    public CommandTabCompleter(String[] subCommands) {
        this.SUBCOMMANDS = subCommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender cmdSender, Command cmd, String s, String[] args) {
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], List.of(SUBCOMMANDS), completions);
        Collections.sort(completions);
        return completions;
    }
}
