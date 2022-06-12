package org.cubeville.commons.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandResponse {
    private List<String> messages = new ArrayList<>();
    
    private boolean baseMessageSet;
    private boolean silent;
    
    public CommandResponse() {
        baseMessageSet = false;
    }
        
    public CommandResponse(String message) {
        messages.add(message);
        baseMessageSet = true;
    }
    
    public CommandResponse(String... messages) {
    	this.messages = new ArrayList<>();
    	for(String message: messages) {
            this.messages.add(message);
    	}
    	baseMessageSet = true;
    }

    public CommandResponse(boolean silent) {
        this.silent = silent;
        baseMessageSet = false;
    }
    
    public void addMessage(String message) {
        messages.add(message);
    }
        
    public void setBaseMessage(String message) {
        if(baseMessageSet) {
            messages.set(0, message);
        }
        else {
            messages.add(0, message);
            baseMessageSet = true;
        }
    }

    public void setBaseMessageIfEmpty(String message) {
        if(messages.size() == 0) {
            setBaseMessage(message);
        }
    }
    
    public List<String> getMessages() {
        if(messages.size() == 0) return null;
        return messages;
    }

    public boolean isSilent() {
        return silent;
    }
}
    
