package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandParameterListString;
import org.cubeville.commons.commands.CommandResponse;

public class For extends BaseCommand {

    public For() {
        super("for");
        addBaseParameter(new CommandParameterListString());
        addBaseParameter(new CommandParameterString());
        for(int i = 0; i < 20; i++) {
            addOptionalBaseParameter(new CommandParameterString());
        }
        setPermission("cvtools.for");
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Player player = (Player)sender;

        List<String> l = (List<String>)baseParameters.get(0);

        for(int i = 0; i < l.size(); i++) {
            for(int c = 1; c < baseParameters.size(); c++) {
                String cmd = (String)baseParameters.get(c);
                String scmd = cmd.replace("%f%", l.get(i));
                player.performCommand(scmd);
            }
        }

        return null;
    }

}
