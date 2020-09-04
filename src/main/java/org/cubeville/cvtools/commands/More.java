package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandResponse;

public class More extends Command {
	
	public More() {
		super("");
	}
	
	@Override
	public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item == null || item.getType() == Material.AIR) { return new CommandResponse("&cThere's enough air in the world, you don't need to hax it in."); }
		if(item.getMaxStackSize() < item.getAmount()) { return new CommandResponse("&cYou have more than you should. I'm not giving you any more."); }
		if(item.getMaxStackSize() == item.getAmount()) { return new CommandResponse("&6You have a full stack in your hand right now."); }
		item.setAmount(item.getMaxStackSize());
		return new CommandResponse("&aItems increased to maximum stack size.");
	}
}
