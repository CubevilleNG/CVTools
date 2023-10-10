package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterOnlinePlayer;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.ColorUtils;

public class Head extends BaseCommand {
    public Head() {
        super("head");
        addBaseParameter(new CommandParameterString());
        addParameter("name", true, new CommandParameterString());
        addParameter("player", true, new CommandParameterOnlinePlayer());
        setPermission("cvtools.head");
    }
    
    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {
        String url = (String) baseParameters.get(0);
        String name = (String) parameters.get("name");
        if(name == null) name = "Custom Head";
        Player player;
        if(parameters.containsKey("player")) {
            player = (Player) parameters.get("player");
        }
        else if(sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            throw new CommandExecutionException("Player parameter must be set if using on console.");
        }
        String command = "minecraft:give " + player.getName() + " minecraft:player_head{display:{Name:\"" + ColorUtils.addColor(name) + "\"},SkullOwner:{Id:\"" + UUID.randomUUID() + "\", Properties:{textures:[{Value:\"" + url + "\"}]}}}";
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        return null;
    }
}
