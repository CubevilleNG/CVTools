package org.cubeville.cvtools;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Levelled;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;
import org.cubeville.commons.commands.StringCommandOutputProcessor;

import org.cubeville.cvtools.commands.*;
import org.cubeville.cvtools.heads.HeadDB;
import org.cubeville.cvtools.heads.HeadManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;

public class CVTools extends JavaPlugin implements Listener {

    private CommandParser commandParser;

    private HeadDB headDB;
    private HeadManager headManager;

    private static CVTools instance;

    class CommandParserRegistryEntry {
        CommandParserRegistryEntry(String permission, CommandParser commandParser) {
            this.commandParser = commandParser;
            this.permission = permission;
        }
        CommandParser commandParser;
        String permission;
    }
    Map<String, CommandParserRegistryEntry> commandParserRegistry = new HashMap<>();

    public void onEnable() {
        instance = this;

        final File dataDir = getDataFolder();
        if(!dataDir.exists()) {
            dataDir.mkdirs();
        }

        headDB = new HeadDB(this);
        try {
            headDB.createBackup(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        headDB.load();
        headManager = new HeadManager(this, headDB);

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
        commandParser.addCommand(new SpawnFrog());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
    }

    public static CVTools getInstance() {
        return instance;
    }
    
    public void registerCommandParser(String command, String permission, CommandParser commandParser) {
        commandParserRegistry.put(command, new CommandParserRegistryEntry(permission, commandParser));
    }

    public String runCommand(String playerUUID, String command) {
        String pcmd = command;
        String args = "";
        
        {
            int idx = pcmd.indexOf(' ');
            if(idx != -1) {
                args = pcmd.substring(idx + 1);
                pcmd = pcmd.substring(0, idx);
            }
        }

        if(! commandParserRegistry.containsKey(pcmd)) {
            return "&cCommand not registered.";
        }

        CommandSender cs = null;
        if(playerUUID != null) {
            cs = Bukkit.getPlayer(UUID.fromString(playerUUID));
        }
        if(cs == null) return "&cPlayer not found.";

        CommandParserRegistryEntry cpre = commandParserRegistry.get(pcmd);
        if(! cs.hasPermission(cpre.permission)) {
            return "&cNo permission.";
        }

        CommandParser cp = cpre.commandParser;
        StringCommandOutputProcessor cop = new StringCommandOutputProcessor();

        String aargs[] = { args };
        cp.execute(cs, aargs, cop);

        return cop.toString();
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

    @EventHandler
    public void onMobSpawnerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(event.getPlayer().isOp() || event.getPlayer().hasPermission("mobeggspawnerblocker.override")) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(block == null) return;
        if(!block.getType().equals(Material.SPAWNER)) return;
        ItemStack item = event.getItem();
        if(item == null) return;
        if(!(item.getItemMeta() instanceof SpawnEggMeta)) return;
        CreatureSpawner cs = (CreatureSpawner) block.getState();
        final Location loc = cs.getLocation();
        final EntityType type = cs.getSpawnedType();
        event.getPlayer().sendMessage(org.bukkit.ChatColor.RED + "Changing spawners using mob eggs is disabled on this server");
        event.setCancelled(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            Block block1 = loc.getBlock();
            if(block1 == null || !block1.getType().equals(Material.SPAWNER)) return;
            CreatureSpawner cs1 = (CreatureSpawner) block1.getState();
            cs1.setSpawnedType(type);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAllaySpawn(EntitySpawnEvent event) {
        if(event.isCancelled()) return;
        if(!event.getEntityType().equals(EntityType.ALLAY)) return;
        if(event.getLocation().getWorld() == null) return;
        String world = event.getLocation().getWorld().getName();
        if(world.equalsIgnoreCase("creative") || world.equalsIgnoreCase("bb2023_update")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHeadPlace(BlockPlaceEvent event) {
        if(event.isCancelled()) return;
        Block block = event.getBlock();
        if(!block.getType().equals(Material.PLAYER_HEAD) && !block.getType().equals(Material.PLAYER_WALL_HEAD)) return;
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if(meta == null) return;
        if(((SkullMeta)meta).getOwningPlayer() == null) return;
        UUID player = ((SkullMeta) meta).getOwningPlayer().getUniqueId();
        String name = meta.getDisplayName();
        List<String> lore = meta.getLore();
        headManager.addHead(new org.cubeville.cvtools.heads.Head(block.getLocation(), player, name, lore));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHeadBreak(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        Block block = event.getBlock();
        if(!block.getType().equals(Material.PLAYER_HEAD) && !block.getType().equals(Material.PLAYER_WALL_HEAD)) return;
        org.cubeville.cvtools.heads.Head head = headManager.getHead(block.getLocation());
        if(head == null) return;
        event.setDropItems(false);
        headManager.removeHead(head);
        if(!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) return;
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(head.getPlayer()));
        meta.setDisplayName(head.getName());
        meta.setLore(head.getLore());
        item.setItemMeta(meta);
        block.getLocation().getWorld().dropItem(block.getLocation(), item);
    }
}
