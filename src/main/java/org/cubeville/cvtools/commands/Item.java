package org.cubeville.cvtools.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

public class Item extends BaseCommand {
	
	public Item() {
		super("");
		addBaseParameter(new CommandParameterString());
		addOptionalBaseParameter(new CommandParameterInteger());
		addOptionalBaseParameter(new CommandParameterString());
	}
	
	@Override
	public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
		String materialName = (String) baseParameters.get(0);
		int totalItems;
		if(baseParameters.size() > 1) { totalItems = ((Integer) baseParameters.get(1)).intValue(); }
		else { totalItems = 1; }
		if(totalItems < 1) { throw new CommandExecutionException("&cYou must ask for at least 1 item."); }
		
		String playerName;
		if(baseParameters.size() == 3) {
			if(!sender.hasPermission("cvtools.item.other")) { throw new CommandExecutionException("&cSyntax: /item <material> [amount]"); }
			else { playerName = (String) baseParameters.get(2); }
		}
		else {
			if(!(sender instanceof Player)) { throw new CommandExecutionException("&cThe console sender must use the syntax /item <material> <amount> <player>"); }
			else { playerName = sender.getName(); }
		}
		if(playerName.equalsIgnoreCase("console")) { throw new CommandExecutionException("&cYou cannot send items to the console."); }
		
		Material material = Material.matchMaterial(materialName);
		if(material == null) { throw new CommandExecutionException("&cUnknown material: &6" + materialName); }
		ItemStack item = new ItemStack(material);
		int max = item.getMaxStackSize();
		if(max * 5 < totalItems && !sender.hasPermission("cvtools.item.unlimited")) { throw new CommandExecutionException("&cI think 5 stacks is enough for 1 command."); }
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		int amount = totalItems;
		while(amount > max) {
			itemStacks.add(new ItemStack(material, max));
			amount -= max;
		}
		item.setAmount(amount);
		itemStacks.add(item);
		
		Player player;
		boolean samePlayer;
		if(playerName.equalsIgnoreCase(sender.getName())) {
			player = (Player) sender;
			samePlayer = true;
		}
		else {
			player = Bukkit.getPlayer(playerName);
			samePlayer = false;
		}
		if(player == null) { throw new CommandExecutionException("&cPlayer &6" + playerName + " &cnot found."); }
		playerName = player.getName();
		if(!player.isOnline()) { throw new CommandExecutionException("&cPlayer &6" + playerName + " &cis not online."); }
		
		Iterator<ItemStack> iter = player.getInventory().addItem(itemStacks.toArray(new ItemStack[itemStacks.size()])).values().iterator();
		int leftover = 0;
		while(iter.hasNext()) {
			leftover += iter.next().getAmount();
		}
		
		if(!samePlayer) { player.sendMessage("§aYou have been given " + (totalItems - leftover) + " of " + material.getKey().getKey() + "."); }
		
		CommandResponse response = new CommandResponse();
		response.addMessage("&aGiven " + (totalItems - leftover) + " of " + material.getKey().getKey() + (samePlayer ? "" : " to " + playerName));
		if(leftover != 0) { response.addMessage((samePlayer ? "&eYour " : "&e" + playerName + "'s ") + "inventory was full, so " + leftover + " items could not be given."); }
		return response;
	}
}
