package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandResponse;

public class TimeSet extends Command {
	
	public TimeSet() {
		super("set");
		addFlag("day");
		addFlag("noon");
		addFlag("sunset");
		addFlag("night");
		addFlag("midnight");
		addFlag("sunrise");
		addOptionalBaseParameter(new CommandParameterInteger());
	}
	
	@Override
	public CommandResponse execute(Player sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
		if(!sender.hasPermission("cvtools.time.set")) { throw new CommandExecutionException("&cNo permission."); }
		if(flags.size() > 1 || (flags.size() == baseParameters.size())) { throw new CommandExecutionException("&cSyntax: /time set <day|noon|sunset|night|midnight|sunrise|&ospecific time&r&c>"); }
		
		int time;
		if(flags.contains("day")) { time = 1000; }
		else if(flags.contains("noon")) { time = 6000; }
		else if(flags.contains("sunset")) { time = 12000; }
		else if(flags.contains("night")) { time = 13000; }
		else if(flags.contains("midnight")) { time = 18000; }
		else if(flags.contains("sunrise")) { time = 23000; }
		else { time = ((Integer) baseParameters.get(0)).intValue(); }
		
		if(time < 0 || time > 24000) { throw new CommandExecutionException("&cThe time must be between 0 and 24000, inclusive."); }
		
		World world = sender.getWorld();
		world.setTime((long) time);
		
		int hour = (int) ((time / 1000L + 6L) % 24L);
		int minute = (int) Math.floor((double) (time % 1000L) * (3.0D / 50.0D));
		boolean isMorning = hour < 12L;
		int friendlyHour;
		if(hour == 0) { friendlyHour = 12; }
		else if(hour < 12) { friendlyHour = (int) hour; }
		else if(hour == 12) { friendlyHour = 12; }
		else { friendlyHour = (int) hour - 12; }
		String message = "§bThe current time is now " + friendlyHour + ":" + String.format("%02d", minute) + (isMorning ? "AM" : "PM") + " (" + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ")";
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getWorld().equals(world)) { player.sendMessage(message); }
		}
		return new CommandResponse("&aTime set to " + time);
	}
}
