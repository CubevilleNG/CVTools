package org.cubeville.commons.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class ColorUtils {

    public static String addColor(String message) {
        boolean hasHeader = false;
        if(message.startsWith("&") && message.length() > 1)
            hasHeader = message.length() > 1 && message.startsWith("&") && "0123456789abcder".indexOf(message.charAt(1)) >= 0;
        if(!hasHeader) message = "&f" + message;
        String ret = ChatColor.translateAlternateColorCodes('&', message);
        return ret;
    }

    public static String reverseColor(String message) {
        for(int i = 0; i < message.length() - 1; i++) {
            if(message.charAt(i) == '§' && "0123456789abcdeflonmkr".indexOf(message.charAt(i + 1)) >= 0) {
                message = message.substring(0, i) + "&" + message.substring(i + 1);
            }
        }
        return message;
    }
    
    public static String addColorWithoutHeader(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static List<String> addColor(List<String> strings) {
    	List<String> finalStrings = new ArrayList<String>();
    	
    	for (String string: strings) {
            finalStrings.add(addColor(string));
    	}
    	
        return finalStrings;
    }

    public static String removeColor(String message) {
        return ChatColor.stripColor(message);
    }
    
    public static Color getColorFromRGB(String string) {
    	String[] colors = string.split(",");
    	List<Integer> i = new ArrayList<Integer>();
    	
    	if (colors.length > 3) {
            return null;
    	}
    	
    	for(String color: colors) {
            try {
                i.add(Integer.parseInt(color));
            } catch (NumberFormatException e) {
                return null;
            }
            if (Integer.parseInt(color) < 0 || Integer.parseInt(color) > 255) {
                return null;
            }
    	}
    	
    	return Color.fromRGB(i.get(0), i.get(1), i.get(2));
    }
    public static Color getColorFromString(String string) {
    	if (string.equalsIgnoreCase("aqua"))
            return Color.AQUA;
    	else if (string.equalsIgnoreCase("black"))
            return Color.BLACK;
    	else if (string.equalsIgnoreCase("blue"))
            return Color.BLUE;
    	else if (string.equalsIgnoreCase("fuschia"))
            return Color.FUCHSIA;
    	else if (string.equalsIgnoreCase("gray") || string.equalsIgnoreCase("grey"))
            return Color.GRAY;
    	else if (string.equalsIgnoreCase("green"))
            return Color.GREEN;
    	else if (string.equalsIgnoreCase("lime"))
            return Color.LIME;
    	else if (string.equalsIgnoreCase("maroon"))
            return Color.MAROON;
    	else if (string.equalsIgnoreCase("navy"))
            return Color.NAVY;
    	else if (string.equalsIgnoreCase("olive"))
            return Color.OLIVE;
    	else if (string.equalsIgnoreCase("orange"))
            return Color.ORANGE;
    	else if (string.equalsIgnoreCase("purple"))
            return Color.PURPLE;
    	else if (string.equalsIgnoreCase("red"))
            return Color.RED;
    	else if (string.equalsIgnoreCase("silver"))
            return Color.SILVER;
    	else if (string.equalsIgnoreCase("teal"))
            return Color.TEAL;
    	else if (string.equalsIgnoreCase("white"))
            return Color.WHITE;
    	else if (string.equalsIgnoreCase("yellow"))
            return Color.YELLOW;   	
    	else {
            return null;
    	}
    }
    
    public static ChatColor getChatColorFromString(String string) {
    	try {
            return ChatColor.valueOf(string);
    	} catch (IllegalArgumentException e) {
            return null;
    	}
    }
    
    public static Color getColorFromHex(String string) {
    	//TODO
    	return null;
    }
    
    public static String stripColor(String string) {
    	return ChatColor.stripColor(string);
    }
    
    public static List<String> stripColors(List<String> strings) {
    	List<String> finalStrings = new ArrayList<String>();
    	
    	for (String string: strings) {
            finalStrings.add(stripColor(string));
    	}
    	
        return finalStrings;
    }
}
