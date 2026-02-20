package net.shyvv.shivCore.commands;

import com.google.common.base.Preconditions;
import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.util.ServerUtil;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SCCommand extends BukkitCommand {
    String cmdID;
    TabCompleter completer = null;
    public SCCommand(@NotNull String cmdID, String description, String usageMessage) {
        super(cmdID);
        this.cmdID = cmdID;
        this.description = description;
        this.usageMessage = usageMessage;
        ServerUtil.registerCommand(this);
    }

    public SCCommand(@NotNull String cmdID, String description, String usageMessage, String[] autofill) {
        super(cmdID);
        this.description = description;
        this.usageMessage = usageMessage;
        ServerUtil.registerCommand(this);
        setAutofill(autofill);
    }

    public void setAutofill(String[] tabCompleter) {
        this.completer = new CommandTabCompleter(tabCompleter);
    }

    @NotNull
    @Override
    public java.util.List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws CommandException, IllegalArgumentException {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");

        List<String> completions = null;
        try {
            if (completer != null) {
                completions = completer.onTabComplete(sender, this, alias, args);
            }
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
            for (String arg : args) {
                message.append(arg).append(' ');
            }
            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(ShivCore.getInstance().getDescription().getFullName());
            throw new CommandException(message.toString(), ex);
        }

        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }
        return completions;
    }

    public void sendError(CommandSender cmdSender, String message) {
        cmdSender.sendMessage("§c"+message);
    }

    public void sendMsg(CommandSender cmdSender, String message) {
        cmdSender.sendMessage("§a"+message);
    }
}
