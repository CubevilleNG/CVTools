package org.cubeville.commons.commands;

import java.lang.IllegalArgumentException;

public class CommandParameterEnumOrNull implements CommandParameterType
{
    Class<? extends Enum> enumType;
    String nullString;
    
    public CommandParameterEnumOrNull(Class<? extends Enum> enumType, String nullString) {
        this.enumType = enumType;
        this.nullString = nullString;
    }

    public boolean isValid(String value) {
        if(value.equals(nullString)) return true;
        try {
            Enum<?> theOneAndOnly = Enum.valueOf(enumType, value.toUpperCase());
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;        
    }

    public String getInvalidMessage(String value) {
        return value + " is no valid value!";
    }

    public Object getValue(String value) {
        if(value.equals(nullString)) return null;
        return Enum.valueOf(enumType, value.toUpperCase());
    }
}
