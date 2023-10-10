package org.cubeville.cvtools.heads;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class Head {

    private Location location;
    private UUID player;
    private String name;
    private List<String> lore;

    public Head(Location location, UUID player, String name, List<String> lore) {
        this.location = location;
        this.player = player;
        this.name = name;
        this.lore = lore;
    }

    public Location getLocation() {
        return this.location;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }
}
