package net.shyvv.shivCore.commands;

import net.shyvv.shivCore.util.ServerUtil;
import org.bukkit.command.CommandExecutor;

public abstract class SCCommand {
    CommandExecutor cmdEx;
    String cmdName;
    String[] autoFill = null;
    public SCCommand(boolean b) {
        register();
    }

    public abstract void register();

    public void registerCMD(CommandExecutor commandExecutor, String command, String[] autoFill) {
        cmdEx = commandExecutor;
        cmdName = command;
        this.autoFill = autoFill;
        ServerUtil.COMMANDS.add(this);
    }
    public void registerCMD(CommandExecutor commandExecutor, String command) {
        cmdEx = commandExecutor;
        cmdName = command;
        ServerUtil.COMMANDS.add(this);
    }

    public boolean hasAutofill() {
        return autoFill != null;
    }

    public CommandExecutor getCommandExecutor() {
        return cmdEx;
    }

    public String getCommandName() {
        return cmdName;
    }

    public String[] getAutoFill() {
        return autoFill;
    }
}
