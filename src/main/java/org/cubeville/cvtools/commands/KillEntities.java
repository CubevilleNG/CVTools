package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterEnum;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandParameterWorld;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.BlockUtils;

public class KillEntities extends BaseCommand {
    
    public KillEntities() {
        super("killentities");
        addParameter("wg", true, new CommandParameterString());
        addParameter("r", true, new CommandParameterInteger());
        addFlag("nopassenger");
        addParameter("world", true, new CommandParameterWorld());
        addParameter("type", false, new CommandParameterEnum(EntityType.class));
        addParameter("noplayer", true, new CommandParameterInteger());
        setPermission("cvtools.killentities");
        setSilentConsole();
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        World world = null;
        Location playerLocation = null;
        if(parameters.containsKey("world")) {
            world = (World)parameters.get("world");
            if(parameters.containsKey("r")) throw new CommandExecutionException("Radius and world parameters can't be used together.");
        }
        else if(sender instanceof Player) {
            playerLocation = ((Player)sender).getLocation();
            world = playerLocation.getWorld();
        }
        else throw new CommandExecutionException("World parameter is mandatory on console.");
       
        boolean noPlayerRadiusSet = parameters.containsKey("noplayer");
        int noPlayerRadius = 0;
        if(noPlayerRadiusSet) noPlayerRadius = (Integer) parameters.get("noplayer");

        boolean noPassenger = flags.contains("nopassenger");

        ProtectedRegion region = null;
        int radius = -1;
        if(parameters.get("wg") != null) {
            if(parameters.containsKey("r")) throw new CommandExecutionException("r and wg parameters are exclusive.");
            region = BlockUtils.getWGRegion(world, (String) parameters.get("wg"));
        }
        else if(parameters.containsKey("r")) {
            if(playerLocation == null) throw new CommandExecutionException("r parameter can only be used by player.");
            radius = (Integer) parameters.get("r");
        }
        else throw new CommandExecutionException("Either wg or r parameters must be used.");
        
        int ret = 0;
        
        List<Entity> entities = world.getEntities();
        for(Entity entity: entities) {
            if(entity.getType() != parameters.get("type")) continue;

            if(region != null) {
                if(!region.contains(entity.getLocation().getBlockX(), entity.getLocation().getBlockY(), entity.getLocation().getBlockZ())) continue;
            }
            else
                if(playerLocation.distance(entity.getLocation()) > radius) continue;
            
            if(noPassenger == true && entity.getPassengers().size() != 0) continue;
            if(noPlayerRadiusSet) {
                List<Player> players = world.getPlayers();
                boolean playerInRadius = false;
                for(Player p: players) {
                    if(p.getLocation().distance(entity.getLocation()) < noPlayerRadius) {
                        playerInRadius = true;
                        break;
                    }
                }
                if(playerInRadius) continue;
            }
            entity.remove();
            ret++;
        }

        return new CommandResponse("&a" + (ret > 0 ? ret : "No") + " entities removed.");
    }

}
