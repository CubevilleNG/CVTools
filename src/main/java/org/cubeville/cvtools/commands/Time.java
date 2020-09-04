package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandResponse;

public class Time extends Command {
	
	public Time() {
		super("");
	}
	
	@Override
	public CommandResponse execute(Player sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
		long currentTime = sender.getWorld().getTime();
		int hour = (int) ((currentTime / 1000L + 6L) % 24L);
		int minute = (int) Math.floor((double) (currentTime % 1000L) * (3.0D / 50.0D));
		boolean isMorning = hour < 12L;
		int friendlyHour;
		if(hour == 0) { friendlyHour = 12; }
		else if(hour < 12) { friendlyHour = (int) hour; }
		else if(hour == 12) { friendlyHour = 12; }
		else { friendlyHour = (int) hour - 12; }
		return new CommandResponse("&bCurrent Time: " + friendlyHour + ":" + String.format("%02d", minute) + (isMorning ? "AM" : "PM") + " (" + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ")");
	}
}
