package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

public class Clear extends Command {
	
	public Clear() {
		super("");
		addFlag("-a");
		addFlag("-s");
		addOptionalBaseParameter(new CommandParameterString());
	}
	
	@Override
	public CommandResponse execute(Player sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
		
		boolean all = flags.contains("-a");
		boolean slot = flags.contains("-s");
		boolean self = baseParameters.isEmpty();
		
		if(all && !sender.hasPermission("cvtools.clear.all")) { return new CommandResponse("&cNo permission."); }
		if(slot && !sender.hasPermission("cvtools.clear.slot")) { return new CommandResponse("&cNo permission."); }
		if(!self && !sender.hasPermission("cvtools.clear.other")) { return new CommandResponse("&cNo permission."); }
		
		Player player;
		if(self) {
			player = sender;
		}
		else {
			String name = (String) baseParameters.get(0);
			OfflinePlayer target = Bukkit.getPlayer(name);
			if(target == null) { return new CommandResponse("&cPlayer &6" + name + " &cnot found."); }
			if(!target.getName().equalsIgnoreCase(name)) { return new CommandResponse("&cPlayer &6" + name + " &cnot found."); }
			name = target.getName();
			player = target.getPlayer();
			if(player == null) { return new CommandResponse("&6" + name + " &cis not online."); }
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(self ? "&aYour " : "&6" + player.getName() + "'s &a");
		if(all) { sb.append("entire inventory"); }
		else if(slot) { sb.append("main hand inventory slot"); }
		else { sb.append("inventory"); }
		sb.append(" has been cleared.");
		
		if(all) { player.getInventory().clear(); }
		else if(slot) { player.getInventory().setItemInMainHand(null); }
		else { for(int i = 9; i < 36; i++) { player.getInventory().setItem(i, null); } }
		
		return new CommandResponse(sb.toString());
	}
}
