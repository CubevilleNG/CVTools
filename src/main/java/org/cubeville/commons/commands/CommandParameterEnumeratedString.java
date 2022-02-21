package org.cubeville.commons.commands;

import java.util.HashSet;
import java.util.Set;

public class CommandParameterEnumeratedString implements CommandParameterType
{
    Set<String> values;

    public CommandParameterEnumeratedString(Set<String> values) {
        this.values = values;
    }

    public CommandParameterEnumeratedString(String...values) {
        this.values = new HashSet<>();
        for(String value: values)
            this.values.add(value);
    }
    
    public boolean isValid(String value) {
        return values.contains(value);
    }

    public String getInvalidMessage(String value) {
        return value + " is no valid parameter.";
    }

    public Object getValue(String value) {
        return value;
    }
}
