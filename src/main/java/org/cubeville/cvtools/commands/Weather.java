package org.cubeville.cvtools.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandParameterWorld;
import org.cubeville.commons.commands.CommandResponse;

public class Weather extends BaseCommand {
	
	public Weather() {
		super("");
		addFlag("-s");
		addBaseParameter(new CommandParameterString());
		addOptionalBaseParameter(new CommandParameterInteger());
		addOptionalBaseParameter(new CommandParameterWorld());
	}
	
	@Override
	public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
		boolean isPlayer = sender instanceof Player;
		
		boolean silent;
		if(flags.contains("-s")) {
			if(sender.hasPermission("cvtools.weather.silent")) { silent = true; }
			else { throw new CommandExecutionException("&cYou do not have permission to make the weather change silent."); }
		}
		else {
			silent = false;
		}
		
		String weather = ((String) baseParameters.get(0)).toLowerCase();
		boolean raining;
		boolean thundering;
		List<String> clear = Arrays.asList("clear", "sun", "sunny", "sunshine");
		List<String> rain = Arrays.asList("rain", "raining", "rainy", "rainstorm", "showers", "snow", "snowy", "snowing");
		List<String> storm = Arrays.asList("storm", "storming", "stormy", "thunder", "lightning", "thunderstorm", "thundershower", "thundershowers", "thundersnow");
		if(clear.contains(weather)) {
			raining = false;
			thundering = false;
		}
		else if(rain.contains(weather)) {
			raining = true;
			thundering = false;
		}
		else if(storm.contains(weather)) {
			raining = true;
			thundering = true;
		}
		else {
			throw new CommandExecutionException("&cUnknown weather type &6" + weather);
		}
		
		int duration;
		if(baseParameters.size() == 2) {
			if(sender.hasPermission("cvtools.weather.duration")) { duration = ((Integer) baseParameters.get(1)).intValue(); }
			else { throw new CommandExecutionException("&cYou do not have permission to specify the duration."); }
		}
		else {
			if(!isPlayer) { throw new CommandExecutionException("&cThe console must specify the duration of the weather event."); }
			else { duration = -1; }
		}
		if(duration < -1) {
			throw new CommandExecutionException("&cDuration must be at least 0 ticks, or -1 for a normal weather event duration.");
		}
		
		World world;
		if(baseParameters.size() == 3) {
			if(sender.hasPermission("cvtools.weather.world")) { world = (World) baseParameters.get(2); }
			else { throw new CommandExecutionException("&cYou do not have permission to specify the world."); }
		}
		else {
			if(!isPlayer) { throw new CommandExecutionException("&cThe console must specify the world in which to change the weather."); }
			else { world = ((Player) sender).getWorld(); }
		}
		if(world.getEnvironment() != World.Environment.NORMAL) {
			throw new CommandExecutionException("&cYou cannot change the weather in &6" + world.getName() + "&c, it is the " + (world.getEnvironment() == World.Environment.NETHER ? "nether." : "end."));
		}
		if(!sender.hasPermission("cvtools.weather.world." + world.getName())) {
			throw new CommandExecutionException("&cYou do not have permission to change the weather in the world &6" + world.getName());
		}
		
		world.setStorm(raining);
		world.setThundering(thundering);
		if(duration != -1) { world.setWeatherDuration(duration); }
		if(thundering && duration != -1) { world.setThunderDuration(duration); }
		
		if(!silent) {
			String message = "§e" + (isPlayer ? sender.getName() : "The Server") + " has ";
			if(thundering) { message += "started a thunderstorm on "; }
			else if(raining) { message += "brought rain to "; }
			else { message += "cleared the sky on "; }
			message += "'" + world.getName() + "'.";
			for(Player player : Bukkit.getOnlinePlayers()) { player.sendMessage(message); }
		}
		if(thundering) { return new CommandResponse("&eWOO LIGHTNING! TIME TO ZAP SOMEONE!"); }
		else if(raining) { return new CommandResponse("&b&oAnd so the plains started flooding with the torrential downpours..."); }
		else { return new CommandResponse("&aYou should go garden while it's nice out."); }
	}
}
