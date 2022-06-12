package org.cubeville.commons.commands;

import org.cubeville.commons.utils.ColorUtils;

public class CommandParameterColor implements CommandParameterType
{
    public boolean isValid(String value) {
        if (ColorUtils.getColorFromString(value.toUpperCase()) != null) {
            return true;
        } else {
            return ColorUtils.getColorFromRGB(value) != null;
        }
    }
    
    public String getInvalidMessage(String value) {
        return value + " is no valid color!";
    }
    
    public Object getValue(String value) {
        if (ColorUtils.getColorFromString(value.toUpperCase()) != null) {
            return ColorUtils.getColorFromString(value.toUpperCase());
        } else {
            return ColorUtils.getColorFromRGB(value);
        }
    }
}
