package net.shyvv.shivCore.constants;

import net.shyvv.shivCore.commands.OpenUICommand;

public class Commands {
    public static OpenUICommand OPEN_UI_COMMAND = new OpenUICommand("openui", "Shows the GUI of the ID entered", "/openui <guiID>");

    public static OpenUICommand GIVE_ITEM_COMMAND = new OpenUICommand("giveitem", "Gives the custom item of the ID entered", "/giveitem <itemID>");

    public static void initialize() {}
}
