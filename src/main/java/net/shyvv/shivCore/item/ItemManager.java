package net.shyvv.shivCore.item;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.constants.Commands;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static List<SCItem> CUSTOM_ITEMS = new ArrayList<>();

    public static void initialize() {
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(new ItemListener(), ShivCore.getInstance());

        List<String> autofillList = new ArrayList<>();
        for(SCItem sci : CUSTOM_ITEMS) {
            autofillList.add(sci.getItemID());
        }
        String[] str = new String[autofillList.size()];
        for(int i = 0; i < autofillList.size(); i++) {
            str[i] = autofillList.get(i);
        }
        Commands.GIVE_ITEM_COMMAND.setAutofill(str);
    }
}
