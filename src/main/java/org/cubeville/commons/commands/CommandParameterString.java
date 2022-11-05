package org.cubeville.commons.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParameterString implements CommandParameterType
{
    Pattern pattern = null;
    
    public CommandParameterString() {
        
    }
    
    public CommandParameterString(String s) {
        pattern = Pattern.compile(s);
    }
    
    public boolean isValid(String value) {
    	if (pattern != null) {
            Matcher m = pattern.matcher(value);
            return (!m.find());
    	} else {
            return true;
    	}
    }
    
    public String getInvalidMessage(String value) {
    	if (pattern != null) {
            return "Invalid characters in string!";
    	} else {
            return "";
    	}
    }
    
    public Object getValue(String value) {
        return value;
    }
    
    public static final String NO_SPECIAL_CHARACTERS = "[^A-Za-z0-9_+$]";
}
