package net.shyvv.shivCore.ui;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.constants.Commands;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    public static List<InvUserInterface> USER_INTERFACES = new ArrayList<>();

    public static void initialize() {
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(new InvUIListener(), ShivCore.getInstance());

        List<String> autofillList = new ArrayList<>();
        for(InvUserInterface iui : USER_INTERFACES) {
            autofillList.add(iui.getID());
        }
        String[] str = new String[autofillList.size()];
        for(int i = 0; i < autofillList.size(); i++) {
            str[i] = autofillList.get(i);
        }
        Commands.OPEN_UI_COMMAND.setAutofill(str);
    }
}
