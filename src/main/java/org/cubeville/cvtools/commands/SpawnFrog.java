package org.cubeville.cvtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpawnFrog extends BaseCommand {

    public SpawnFrog() {
        super("spawnfrog");
        addParameter("player", false, new CommandParameterOnlinePlayer());
        addParameter("type", false, new CommandParameterString());
        setPermission("cvtools.spawnfrog");
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        Frog.Variant type;
        if (parameters.get("type").toString().equalsIgnoreCase("cold")) {
            type = Frog.Variant.COLD;
        } else if (parameters.get("type").toString().equalsIgnoreCase("temperate")) {
            type = Frog.Variant.TEMPERATE;
        } else if (parameters.get("type").toString().equalsIgnoreCase("warm")) {
            type = Frog.Variant.WARM;
        } else {
            throw new CommandExecutionException("Valid type are cold, temperate, and warm!");
        }
        Player player = (Player) parameters.get("player");
        Location location = player.getTargetBlock(null, 5).getLocation().add(0.5, 1, 0.5);
        Frog frog = (Frog) player.getWorld().spawnEntity(location, EntityType.FROG);
        frog.setVariant(type);
        if(location.getWorld().getLivingEntities().contains(frog)) {
            return new CommandResponse(parameters.get("type") + " frog spawned at " + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getWorld().getName());
        } else {
            throw new CommandExecutionException("Unable to spawn frog here! Check mobcap or region permissions!");
        }
    }
}
