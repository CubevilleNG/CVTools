package org.cubeville.cvtools;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

import org.cubeville.cvtools.commands.*;

public class CVTools extends JavaPlugin implements Listener {

    private CommandParser commandParser;

    public void onEnable() {
        commandParser = new CommandParser();
        commandParser.addCommand(new ChatColor());
        commandParser.addCommand(new CheckEntities());
        commandParser.addCommand(new CheckRegionPlayers());
        commandParser.addCommand(new CheckSign());
        commandParser.addCommand(new Clear());
        commandParser.addCommand(new CountEntities());
        commandParser.addCommand(new DelayedTask(this));
        DistanceTask distanceTask = new DistanceTask(this);
        commandParser.addCommand(new Distance(distanceTask));
        commandParser.addCommand(new FindBlocks());
        commandParser.addCommand(new For());
        commandParser.addCommand(new Head());
        commandParser.addCommand(new Info());
        commandParser.addCommand(new Item());
        commandParser.addCommand(new Itemname());
        commandParser.addCommand(new KillEntities());
        commandParser.addCommand(new More());
        commandParser.addCommand(new PathBlockUtil());
        commandParser.addCommand(new Syslog());
        commandParser.addCommand(new Time());
        commandParser.addCommand(new TimeSet());
        commandParser.addCommand(new Title());
        commandParser.addCommand(new Weather());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cvtools")) {
            return commandParser.execute(sender, args);
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
        return false;
    }

    @EventHandler
    public void onLightBlockInteract(PlayerInteractEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getClickedBlock() == null) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(event.getItem() == null || !event.getItem().equals(new ItemStack(Material.LIGHT))) return;
        if(!event.getClickedBlock().getType().equals(Material.LIGHT)) return;
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || !event.getPlayer().hasPermission("cvtools.changelightblock")) return;
        Block lightBlock = event.getClickedBlock();
        Levelled level = (Levelled) lightBlock.getBlockData();
        int i = level.getLevel() + 1 == 16 ? 0 : level.getLevel() + 1;
        level.setLevel(i);
        lightBlock.setBlockData(level, true);
    }
}
