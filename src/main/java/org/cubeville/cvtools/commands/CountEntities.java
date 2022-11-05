package org.cubeville.cvtools.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterEnum;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandResponse;

public class CountEntities extends Command {

    public CountEntities() {
        super("countentities");
        addParameter("r", true, new CommandParameterInteger());
        addParameter("type", false, new CommandParameterEnum(EntityType.class));
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        int radius = 10;
        if(parameters.containsKey("r")) radius = (int) parameters.get("r");

        EntityType type = (EntityType) parameters.get("type");

        Location loc = player.getLocation();

        List<Entity> entities = loc.getWorld().getEntities();
        int cnt = 0;
        for(Entity e: entities) {
            if(e.getType() != type) continue;
            if(e.getLocation().distance(loc) <= radius) {
                cnt++;
            }
        }

        if(cnt == 0)
            return new CommandResponse("&cFound no entities of type " + type);
        return new CommandResponse("&aFound " + cnt + " entities of type " + type);
    }
}
