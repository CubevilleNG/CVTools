package org.cubeville.cvtools.heads;

import org.bukkit.Location;

import java.util.List;

public class Head {

    private Location location;
    private String name;
    private List<String> lore;

    public Head(Location location, String name, List<String> lore) {
        this.location = location;
        this.name = name;
        this.lore = lore;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }
}
