package org.cubeville.commons.commands;

import java.util.List;
import java.util.ArrayList;

public class StringCommandOutputProcessor implements CommandOutputProcessor
{
    private List<String> response;

    public StringCommandOutputProcessor() {
        response = new ArrayList<>();
    }
    
    public void sendMessage(String message) {
        response.add(message);
    }

    public String toString() {
        return String.join("\n", response);
    }
}
