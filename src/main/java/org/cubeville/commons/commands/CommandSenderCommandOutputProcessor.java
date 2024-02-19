package org.cubeville.commons.commands;

import org.bukkit.command.CommandSender;

public class CommandSenderCommandOutputProcessor implements CommandOutputProcessor
{
    private CommandSender commandSender;

    CommandSenderCommandOutputProcessor(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public void sendMessage(String message) {
        commandSender.sendMessage(message);
    }

    
}
