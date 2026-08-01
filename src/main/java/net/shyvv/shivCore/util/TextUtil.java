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
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (c == ' ') {
                if (line.length() + word.length() + (!line.isEmpty() ? 1 : 0) > breakpoint) {
                    ltc.add(Component.text(line.toString()));
                    line = new StringBuilder();
                }
                if (!line.isEmpty()) line.append(' ');
                line.append(word);
                word = new StringBuilder();
            } else {
                word.append(c);
            }
        }

        if (line.length() + word.length() + (!line.isEmpty() ? 1 : 0) > breakpoint) {
            ltc.add(Component.text(line.toString()));
            line = new StringBuilder();
        }
        if (!line.isEmpty()) line.append(' ');
        line.append(word);

        if (!line.isEmpty()) {
            ltc.add(Component.text(line.toString()));
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

    public static List<TextComponent> indexComponentList(List<TextComponent> ltc, int indexAmount) {
        List<TextComponent> nltc = new ArrayList<>();
        TextComponent index = Component.text("");
        for(int i = 1; i<=indexAmount; i++) {
            index = index.append(Component.text(" "));
        }
        for(int i = 0; i < ltc.size(); i++) {
            ltc.set(i, index.append(ltc.get(i)));
        }
        return ltc;
    }
}
