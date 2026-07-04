package net.shyvv.shivCore.constants;

import net.shyvv.shivCore.commands.*;

public class Commands {
    public static OpenUICommand OPEN_UI_COMMAND = new OpenUICommand("openui", "Shows the GUI of the ID entered", "/openui <guiID>");
    public static SpawnEntityCommand SPAWN_ENTITY_COMMAND = new SpawnEntityCommand("spawnentity", "Spawns a custom entity of the ID entered", "/spawnentity <entityID>");
    public static CustomDamageCommand CUSTOM_DAMAGE_COMMAND = new CustomDamageCommand("customdamage", "Damages yourself with the ID of the damage entered", "/customdamage <damageID>");
    public static GiveItemCommand GIVE_ITEM_COMMAND = new GiveItemCommand("giveitem", "Gives the custom item of the ID entered", "/giveitem <itemID>");
    public static TrustCommand TRUST_COMMAND = new TrustCommand("trust", "Adds, removes, clears and lists the players in your trusted list", "/trust <add/remove/list/clear> <args>");

    public static void initialize() {}
}
