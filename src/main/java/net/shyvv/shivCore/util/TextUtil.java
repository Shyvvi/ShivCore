package net.shyvv.shivCore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class TextUtil {

    public static List<TextComponent> getFitTextComponent(String string, int breakpoint) {
        List<TextComponent> ltc = new ArrayList<>();
        char[] text = string.toCharArray();
        String s = "";
        int j = 0;
        for(int i = 0; i < text.length; i++) {
            j++;
            s += text[i];
            if(j >= breakpoint || i == text.length-1) {
                ltc.add(Component.text(s));
                s = "";
                j = 0;
            }
        }
        return ltc;
    }

    public static List<TextComponent> decorateComponentList(List<TextComponent> ltc, TextDecoration decoration, boolean value, IntRange exemptedIndexRange) {
        for(int i = exemptedIndexRange.getFirstInt(); i < exemptedIndexRange.secondInt; i++) {
            TextComponent tc = ltc.get(i);
            ltc.set(i, tc.decoration(decoration, value));
        }
        return ltc;
    }

    public static List<TextComponent> decorateComponentList(List<TextComponent> ltc, TextDecoration decoration, boolean value) {
        return decorateComponentList(ltc, decoration, value, new IntRange(0, ltc.size()));
    }

    public static List<TextComponent> colorComponentList(List<TextComponent> ltc, TextColor color, IntRange exemptedIndexRange) {
        for(int i = exemptedIndexRange.getFirstInt(); i < exemptedIndexRange.secondInt; i++) {
            TextComponent tc = ltc.get(i);
            ltc.set(i, tc.color(color));
        }
        return ltc;
    }

    public static List<TextComponent> colorComponentList(List<TextComponent> ltc, TextColor color) {
        return colorComponentList(ltc, color, new IntRange(0, ltc.size()));
    }
}
