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
        String cmd = (String)baseParameters.get(1);
        for(int i = 2; i < baseParameters.size(); i++) {
            if(i > 1) cmd += " ";
            cmd += (String)baseParameters.get(i);
        }

        List<String> l = (List<String>)baseParameters.get(0);

        for(int i = 0; i < l.size(); i++) {
            String scmd = cmd.replace("%f%", l.get(i));
            player.performCommand(scmd);
        }
        return null;
    }

}
