package org.cubeville.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class BlockUtils {

    public static List<Block> getBlocksInRadius(Location loc, int radius) {
        Location loc1 = new Location(loc.getWorld(), loc.getBlockX() - radius, loc.getBlockY() - radius, loc.getBlockZ() - radius);
        Location loc2 = new Location(loc.getWorld(), loc.getBlockX() + radius, loc.getBlockY() + radius, loc.getBlockZ() + radius);

        List<Block> blocks = getBlocksInCube(loc1, loc2);
        List<Block> newBlocks = new ArrayList<>();

        for(Block block: blocks) {
            if (block.getLocation().distance(loc) < radius)
                newBlocks.add(block);
        }

        return blocks;
    }

    public static List<Block> getBlocksInCube(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();

        for (int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
            for (int y = loc1.getBlockY(); y <= loc2.getBlockY(); y++) {
                for (int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); z++) {
                    blocks.add(new Location(loc1.getWorld(),x,y,z).getBlock());
                }
            }
        }

        return blocks;
    }

    public static List<Block> getBlocksInWESelection(Player player, int limit) {
        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Region selection;
        try {
            selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
        }
        catch(IncompleteRegionException e) {
            throw new IllegalArgumentException("Region selection incomplete.");
        }
        if(selection == null) throw new IllegalArgumentException("No region selected.");

        World world = BukkitAdapter.adapt(worldEdit.getSession(player).getSelectionWorld());
        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();
        List<Block> blocks = new ArrayList<>();

        int width = max.getX() - min.getX() + 1;
        int height = max.getY() - min.getY() + 1;
        int length = max.getZ() - min.getZ() + 1;
        if(width * length * height > limit) throw new IllegalArgumentException("Limit of " + limit + " reached!");

        for(int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for(int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    if(selection.contains(BlockVector3.at(x, y, z))) {
                        blocks.add(world.getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Block> getBlocksInWGRegion(World world, String name, int limit) {
        ProtectedRegion region = getWGRegion(world, name);

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();
        List<Block> blocks = new ArrayList<>();

        int width = max.getBlockX() - min.getBlockX() + 1;
        int height = max.getBlockY() - min.getBlockY() + 1;
        int length = max.getBlockZ() - min.getBlockZ() + 1;
        if(width * length * height > limit) throw new IllegalArgumentException("Limit of " + limit + " reached!");
                                                
        for(int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for(int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    if(region.contains(BlockVector3.at(x, y, z))) {
                        blocks.add(world.getBlockAt(x, y, z));
                    }
                }
            }                
        }
        return blocks;
    }

    public static int getWESelectionArea(Player player) {
        Region sel = getWESelection(player);
        BlockVector3 min = sel.getMinimumPoint();
        BlockVector3 max = sel.getMaximumPoint();
        int y = min.getBlockY();
        int xmin = min.getBlockX();
        int xmax = max.getBlockX();
        int zmin = min.getBlockZ();
        int zmax = max.getBlockZ();
        int cnt = 0;
        for(int x = xmin; x <= xmax; x++) {
            for(int z = zmin; z <= zmax; z++) {
                if(sel.contains(BlockVector3.at(x, y, z))) cnt++;
            }
        }
        return cnt;
    }
    
    public static void setWESelection(Player player, World world, Vector pos1, Vector pos2) {
        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        BlockVector3 wep1 = BlockVector3.at(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ());
        BlockVector3 wep2 = BlockVector3.at(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ());
        CuboidRegionSelector selector = new CuboidRegionSelector(BukkitAdapter.adapt(world), wep1, wep2);
        worldEdit.getSession(player).setRegionSelector(BukkitAdapter.adapt(world), selector);
        //Selection selection = new CuboidSelection(world, wep1, wep2);
        //worldEdit.setSelection(player, selection);
    }

    public static Region getWESelection(Player player) {
        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Region selection = null;
        try {
            selection = worldEdit.getSession(player).getSelection(worldEdit.getSession(player).getSelectionWorld());
        }
        catch(IncompleteRegionException e) {
            throw new IllegalArgumentException("Incomplete region selection.");
        }
        if(selection == null) throw new IllegalArgumentException("No region selected.");
        return selection;
    }
    
    public static Location getWESelectionMin(Player player) {
        BlockVector3 min = getWESelection(player).getMinimumPoint();
        return new Location(BukkitAdapter.adapt(getWESelection(player).getWorld()), min.getX(), min.getY(), min.getZ());
    }

    public static Location getWESelectionMax(Player player) {
        BlockVector3 min = getWESelection(player).getMaximumPoint();
        return new Location(BukkitAdapter.adapt(getWESelection(player).getWorld()), min.getX(), min.getY(), min.getZ());
    }

    public static List<Entity> getEntitiesInWESelection(Player player) {
        Vector min = getWESelectionMin(player).toVector();
        Vector max = getWESelectionMax(player).toVector();
        max.setX(max.getX() + 0.999);
        max.setY(max.getY() + 0.999);
        max.setZ(max.getZ() + 0.999);

        List<Entity> ret = new ArrayList<>();
        for(Entity e: player.getLocation().getWorld().getEntities()) {
            if(e.getLocation().toVector().isInAABB(min, max))
                ret.add(e);
        }
        return ret;
    }

    public static List<Entity> getEntitiesWithinRadius(Location loc, double radius) {
        List<Entity> ret = new ArrayList<>();
        for(Entity e: loc.getWorld().getEntities()) {
            if(e.getLocation().distance(loc) <= radius)
                ret.add(e);
        }
        return ret;
    }

    public static List<Entity> filterEntitiesByType(List<Entity> entities, EntityType type) {
        List<Entity> ret = new ArrayList<>();
        for(Entity e: entities)
            if(e.getType() == type)
                ret.add(e);
        return ret;
    }
    
    public static ProtectedRegion getWGRegion(World world, String name) {
        WorldGuardPlugin worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        ProtectedRegion region = regionManager.getRegion(name);
        if(region == null) throw new IllegalArgumentException("No region found by that name!");
        return region;
    }

    public static Vector getWGRegionMin(World world, String name) {
        BlockVector3 min = getWGRegion(world, name).getMinimumPoint();
        return new Vector(min.getX(), min.getY(), min.getZ());
    }

    public static Vector getWGRegionMax(World world, String name) {
        BlockVector3 max = getWGRegion(world, name).getMaximumPoint();
        return new Vector(max.getX(), max.getY(), max.getZ());
    }

    public static boolean isInRegion(String region, Location loc) {
        try {
            ProtectedRegion r = getWGRegion(loc.getWorld(), region);
            return r.contains(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        }
        catch(IllegalArgumentException e) {
            return false;
        }
    }

    public static String findApplicableRegion(Set<String> regions, Location location) {
        List<String> ret = new ArrayList<>();
        for(String region: regions) {
            if(isInRegion(region, location)) {
                return region;
            }
        }
        return null;
    }
    
    public static List<Block> getBlocksInRadiusByType(Location loc, int radius, Material... mats) {
        List<Block> blocks = getBlocksInRadius(loc, radius);
        return getBlocksByType(blocks, mats);
    }

    public static List<Block> getBlocksInRadiusByType(Location loc, int radius, Set<Material> materials) {
        List<Block> blocks = getBlocksInRadius(loc, radius);
        return getBlocksByType(blocks, materials);
    }
    
    public static List<Block> getBlocksInCubeByType(Location loc1, Location loc2, Material... mats) {
        List<Block> blocks = getBlocksInCube(loc1, loc2);
        return getBlocksByType(blocks, mats);
    }

    public static List<Block> getBlocksInWESelectionByType(Player player, int limit, Material... mats) {
        List<Block> blocks = getBlocksInWESelection(player, limit);
        return getBlocksByType(blocks, mats);
    }

    public static List<Block> getBlocksInWESelectionByType(Player player, int limit, Set<Material> materials) {
        List<Block> blocks = getBlocksInWESelection(player, limit);
        return getBlocksByType(blocks, materials);
    }
    
    public static List<Block> getBlocksInWGRegionByType(Player player, String name, int limit, Material... mats) {
        return getBlocksInWGRegionByType(player.getWorld(), name, limit, mats);
    }

    public static List<Block> getBlocksInWGRegionByType(World world, String name, int limit, Material... mats) {
        List<Block> blocks = getBlocksInWGRegion(world, name, limit);
        return getBlocksByType(blocks, mats);
    }

    public static List<Block> getBlocksInWGRegionByType(World world, String name, int limit, Set<Material> materials) {
        List<Block> blocks = getBlocksInWGRegion(world, name, limit);
        return getBlocksByType(blocks, materials);
    }

    public static List<Block> getBlocksInWGRegionByType(Player player, String name, int limit, Set<Material> materials) {
        return getBlocksInWGRegionByType(player.getWorld(), name, limit, materials);
    }
    
    public static List<Block> getBlocksByType(List<Block> blocks, Material... mats) {
        if(blocks == null) return null;

        List<Block> newBlocks = new ArrayList<>();

        for(Block block: blocks) {
            for(Material mat: mats) {
                if(block.getType() == mat) newBlocks.add(block);
            }
        }

        return newBlocks;
    }

    public static List<Block> getBlocksByType(List<Block> blocks, Set<Material> materials) {
        if(blocks == null) return null;

        List<Block> ret = new ArrayList<>();

        for(Block block: blocks) {
            if(materials.contains(block.getType())) ret.add(block);
        }

        return ret;
    }
    
    public static Block getNearestBlock(List<Block> blocks, Location loc) {
    	Block nearestBlock = null;
    	double distance = 10000;
    	
        if(blocks == null) return null;
    	
    	for(Block block: blocks) {
    		if (block.getLocation().distance(loc) < distance) {
    			nearestBlock = block;
    			distance = block.getLocation().distance(loc);
    		}
    	}
    	
		return nearestBlock;
    	
    }

}
