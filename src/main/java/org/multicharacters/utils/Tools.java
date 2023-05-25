package org.multicharacters.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tools {
    public static void sendMessage(Player player, String message) {
        player.sendMessage(Tools.translate(message));
    }

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)s);
    }

    public static List<String> translate(List<String> s) {
        ArrayList<String> lists = new ArrayList<String>();
        for (String s1 : s) {
            lists.add(Tools.translate(s1));
        }
        return lists;
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static String doubleDigitNumber(double number) {
        return new DecimalFormat("#.0").format(number);
    }
}
