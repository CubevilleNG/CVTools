package org.cubeville.cvtools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.cubeville.commons.commands.CommandParser;

import org.cubeville.cvtools.commands.*;

public class CVTools extends JavaPlugin implements Listener {
	
	private static final String HAMMER_NAME = "§6Mj" + Character.valueOf((char) 246).toString() + "lnir";
	private static final String AXE_NAME = "§6Stormbreaker";
	
    private CommandParser cvtoolsParser;
    private CommandParser clearParser;
    private CommandParser moreParser;
    private CommandParser itemParser;
    private CommandParser timeParser;
    private CommandParser weatherParser;
    
    private Set<UUID> thorEnabled;

    public void onEnable() {
        cvtoolsParser = new CommandParser();
        cvtoolsParser.addCommand(new ChatColor());
        cvtoolsParser.addCommand(new CheckEntities());
        cvtoolsParser.addCommand(new CheckRegionPlayers());
        cvtoolsParser.addCommand(new CheckSign());
        cvtoolsParser.addCommand(new DelayedTask(this));
        cvtoolsParser.addCommand(new FindBlocks());
        cvtoolsParser.addCommand(new Head());
        cvtoolsParser.addCommand(new Info());
        cvtoolsParser.addCommand(new Itemname());
        cvtoolsParser.addCommand(new KillEntities());
        cvtoolsParser.addCommand(new PathBlockUtil());
        cvtoolsParser.addCommand(new Title());
        
        clearParser = new CommandParser();
        clearParser.addCommand(new Clear());
        
        moreParser = new CommandParser();
        moreParser.addCommand(new More());
        
        itemParser = new CommandParser();
        itemParser.addCommand(new Item());
        
        timeParser = new CommandParser();
        timeParser.addCommand(new Time());
        timeParser.addCommand(new TimeSet());
        
        weatherParser = new CommandParser();
        weatherParser.addCommand(new Weather());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        
        thorEnabled = new HashSet<UUID>();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cvtools")) {
            return cvtoolsParser.execute(sender, args);
        }
        else if(command.getName().equals("cvtoolstest")) {
            // Player player = (Player) sender;
            // Location loc = player.getLocation();
            // loc.add(20, 0, 0);
            // FireworkEffect effect = FireworkEffect.builder().trail(false).flicker(false).withColor(Color.RED).withFade(Color.BLUE).with(FireworkEffect.Type.BALL).build();
            // final Firework fw = player.getLocation().getWorld().spawn(loc, Firework.class);
            // FireworkMeta meta = fw.getFireworkMeta();
            // meta.addEffect(effect);
            // meta.setPower(0);
            // fw.setFireworkMeta(meta);
            // new BukkitRunnable() {
            //     public void run() {
            //         fw.detonate();
            //         //fw.playEffect(EntityEffect.FIREWORK_EXPLODE);
            //         //fw.remove();
            //     }
            // }.runTaskLater(this, 1L);
            Player player = Bukkit.getServer().getPlayer(args[0]);
            sender.sendMessage("Player " + player.getName() + " isdead: " + player.isDead());
        }
        else if(command.getName().equals("ping")) {
        	if(args.length == 0) { sender.sendMessage("§ePong!"); }
        	else { sender.sendMessage("§cSyntax: /ping"); }
        	return true;
        }
        else if(command.getName().equals("shock")) {
        	if(args.length != 1) {
        		sender.sendMessage("§cSyntax: /shock <player>");
        		return true;
        	}
        	Player player = Bukkit.getPlayer(args[0]);
        	if(player == null) {
        		sender.sendMessage("§cPlayer " + args[0] + " not found.");
        		return true;
        	}
        	if(!player.isOnline()) {
        		sender.sendMessage("§cPlayer " + args[0] + " is not online.");
        		return true;
        	}
        	player.getWorld().strikeLightning(player.getLocation());
        	for(Player p : Bukkit.getOnlinePlayers()) { p.sendMessage("§e" + sender.getName() + " shocked " + player.getName()); }
        	return true;
        }
        else if(command.getName().equals("clear")) {
        	return clearParser.execute(sender, args);
        }
        else if(command.getName().equals("more")) {
        	return moreParser.execute(sender, args);
        }
        else if(command.getName().equals("item")) {
        	return itemParser.execute(sender, args);
        }
        else if(command.getName().equals("time")) {
        	return timeParser.execute(sender, args);
        }
        else if(command.getName().equals("weather")) {
        	return weatherParser.execute(sender, args);
        }
        else if(command.getName().equals("thor")) {
        	return toggleThor(sender, args, true);
        }
        else if(command.getName().equals("unthor")) {
        	return toggleThor(sender, args, false);
        }
        else if(command.getName().equals("slap")) {
        	if(args.length != 1) {
        		sender.sendMessage("§cSyntax: /slap <player>");
        		return true;
        	}
        	Player player = Bukkit.getPlayer(args[0]);
        	if(player == null) {
        		sender.sendMessage("§cPlayer " + args[0] + " not found.");
        		return true;
        	}
        	if(!player.isOnline()) {
        		sender.sendMessage("§cPlayer " + args[0] + " is not online.");
        		return true;
        	}
        	Random rand = new Random();
        	player.setVelocity(new Vector(rand.nextDouble() * 2.0D - 1.0D, 1.0D, rand.nextDouble() * 2.0D - 1.0D));
        	for(Player p : Bukkit.getOnlinePlayers()) { p.sendMessage("§e" + sender.getName() + " slapped " + player.getName()); }
        	return true;
        }
        else if(command.getName().equals("rocket")) {
        	if(args.length != 1) {
        		sender.sendMessage("§cSyntax: /rocket <player>");
        		return true;
        	}
        	Player player = Bukkit.getPlayer(args[0]);
        	if(player == null) {
        		sender.sendMessage("§cPlayer " + args[0] + " not found.");
        		return true;
        	}
        	if(!player.isOnline()) {
        		sender.sendMessage("§cPlayer " + args[0] + " is not online.");
        		return true;
        	}
        	player.setVelocity(new Vector(0.0D, 2.0D, 0.0D));
        	for(Player p : Bukkit.getOnlinePlayers()) { p.sendMessage("§e" + sender.getName() + " rocketed " + player.getName()); }
        	return true;
        }
        else if(command.getName().equals("barrage")) {
        	if(args.length != 1) {
        		sender.sendMessage("§cSyntax: /barrage <player>");
        		return true;
        	}
        	Player player = Bukkit.getPlayer(args[0]);
        	if(player == null) {
        		sender.sendMessage("§cPlayer " + args[0] + " not found.");
        		return true;
        	}
        	if(!player.isOnline()) {
        		sender.sendMessage("§cPlayer " + args[0] + " is not online.");
        		return true;
        	}
        	Location loc = player.getLocation().clone();
        	double tau = 2.0D * Math.PI;
        	double arc = tau / 24.0D;
        	for(double a = 0.0D; a < tau; a += arc) {
        		Vector dir = (new Vector(Math.cos(a), 0.0D, Math.sin(a))).normalize();
        		Vector finalDir = loc.toVector().add(dir.multiply(2));
        		Location finalLoc = new Location(loc.getWorld(), finalDir.getX(), finalDir.getY(), finalDir.getZ());
        		Arrow arrow = finalLoc.getWorld().spawn(finalLoc, Arrow.class);
        		arrow.setVelocity(dir.multiply(2.0D));
        		arrow.setShooter(player);
        	}
        	for(Player p : Bukkit.getOnlinePlayers()) { p.sendMessage("§e" + sender.getName() + " barraged " + player.getName()); }
        	return true;
        }
        return false;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	if(!thorEnabled.contains(player.getUniqueId())) { return; }
    	ItemStack item = player.getInventory().getItemInMainHand();
    	if(!isThorItem(item)) { return; }
    	Action action = event.getAction();
    	if(action != Action.LEFT_CLICK_BLOCK && action != Action.LEFT_CLICK_AIR) { return; }
    	Block block = null;
    	if(action == Action.LEFT_CLICK_BLOCK) { block = event.getClickedBlock(); }
    	else { block = player.getTargetBlock((Set<Material>) null, 300); }
    	if(block == null) { return; }
    	player.getWorld().strikeLightning(block.getLocation());
    }
    
    private boolean toggleThor(CommandSender sender, String[] args, boolean enable) {
    	String playerName;
    	if(!(sender instanceof Player)) {
    		if(args.length == 1) {
    			playerName = args[0];
    		}
    		else {
    			sender.sendMessage("§cSyntax: + " + (enable ? "/thor" : "/unthor") + " <player>");
    			return true;
    		}
    	}
    	else {
    		if(args.length == 0) {
    			playerName = sender.getName();
    		}
    		else if(args.length == 1) {
    			playerName = args[0];
    		}
    		else if(sender.hasPermission("cvtools.thor.other")) {
    			sender.sendMessage("§cSyntax: " + (enable ? "/thor" : "/unthor") + " [player]");
    			return true;
    		}
    		else {
    			sender.sendMessage("§cSyntax: " + (enable ? "/thor" : "/unthor"));
    			return true;
    		}
    	}
    	if(playerName.equalsIgnoreCase("console")) {
    		sender.sendMessage("§cThe console cannot have Thor powers" + (enable ? "." : " taken away."));
    		return true;
    	}
    	Player player = getServer().getPlayer(playerName);
    	if(player == null) {
    		sender.sendMessage("§cPlayer §6" + playerName + " §cnot found.");
    		return true;
    	}
    	playerName = player.getName();
    	if(!player.isOnline()) { 
    		sender.sendMessage("§6" + playerName + " §cis not online.");
    		return true;
    	}
    	boolean samePlayer = sender.getName().equalsIgnoreCase(playerName);
    	if(enable) {
    		if(!thorEnabled.add(player.getUniqueId())) {
    			sender.sendMessage("§c" + (samePlayer ? "You" : playerName) + " already " + (samePlayer ? "have" : "has") + " Thor enabled.");
    		}
    		else {
    			boolean hammer = (new Random()).nextBoolean();
    			ItemStack thorItem = new ItemStack(hammer ? Material.IRON_PICKAXE : Material.IRON_AXE);
    			ItemMeta meta = thorItem.getItemMeta();
    			meta.setDisplayName(hammer ? HAMMER_NAME : AXE_NAME);
    			thorItem.setItemMeta(meta);
    			PlayerInventory inv = player.getInventory();
    			ItemStack main = inv.getItemInMainHand();
    			player.sendMessage("Item in main hand: " + main.toString());
    			if(main.getType() != Material.AIR) {
    				boolean moved = false;
    				ItemStack[] contents = inv.getStorageContents();
        			for(int i = 0; i < contents.length; i++) {
        				ItemStack item = contents[i];
        				if(item == null) {
        					player.sendMessage("§bFound null item slot at slot " + i);
        					inv.setItem(i, main);
        					moved = true;
        					break;
        				}
        				if(item.getType() == Material.AIR) {
        					player.sendMessage("§bFound air item slot at slot " + i);
        					inv.setItem(i, main);
        					moved = true;
        					break;
        				}
        				player.sendMessage("§6Found slot at " + i + " that can't be used with mainhand item: " + item.getType().toString());
        			}
        			if(!moved) {
        				player.getWorld().dropItem(player.getLocation(), main);
        			}
    			}
    			inv.setItemInMainHand(thorItem);
    			
    			player.sendMessage("§aYou have been given " + (hammer ? HAMMER_NAME : AXE_NAME) + ". Use it wisely.");
    			if(!samePlayer) { sender.sendMessage("§6" + playerName + " §ahas been given Thor's powers."); }
    		}
    	}
    	else {
    		if(!thorEnabled.remove(player.getUniqueId())) {
    			sender.sendMessage("§c" + (samePlayer ? "You do" : playerName + " does") + " not have Thor enabled.");
    		}
    		else {
    			PlayerInventory inv = player.getInventory();
    			ItemStack[] contents = inv.getStorageContents();
    			for(int i = 0; i < contents.length; i++) {
    				if(isThorItem(contents[i])) { inv.setItem(i, null); }
    			}
    			player.sendMessage("§aYou have had Thor's powers removed.");
    			if(!samePlayer) { sender.sendMessage("§6" + playerName + " §ahas had Thor's powers removed."); }
    		}
    	}
    	return true;
    }
    
    private boolean isThorItem(ItemStack item) {
    	if(item == null) { return false; }
    	if(item.getType() == Material.IRON_PICKAXE) { return item.getItemMeta().getDisplayName().equals(HAMMER_NAME); }
    	else if(item.getType() == Material.IRON_AXE) { return item.getItemMeta().getDisplayName().equals(AXE_NAME); }
    	else { return false; }
    }
}
