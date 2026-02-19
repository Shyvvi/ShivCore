package net.shyvv.shivCore.util.managers;

import net.shyvv.shivCore.ShivCoreMain;
import net.shyvv.shivCore.ui.InvUserInterface;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    public static List<InvUserInterface> USER_INTERFACES = new ArrayList<>();

    public static void initialize() {
        List<String> autofillList = new ArrayList<>();
        for(InvUserInterface iui : USER_INTERFACES) {
            autofillList.add(iui.getID());
        }
        String[] str = new String[autofillList.size()];
        for(int i = 0; i < autofillList.size(); i++) {
            str[i] = autofillList.get(i);
        }
        ShivCoreMain.setCommandAutofill(ShivCoreMain.OPEN_UI_CMD, str);
    }
}
