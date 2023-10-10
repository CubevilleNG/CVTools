package org.cubeville.cvtools.heads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HeadManager {

    private final Plugin plugin;
    private final List<Head> heads;
    private final HeadManager instance;
    private final HeadDB headDB;

    public HeadManager(Plugin plugin, HeadDB headDB) {
        this.plugin = plugin;
        this.headDB = headDB;
        this.heads = new ArrayList<>();
        importHeadsFromDB(headDB);
        instance = this;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public HeadManager getInstance() {
        return this.instance;
    }

    public void importHeadsFromDB(HeadDB headDB) {
        try {
            ResultSet headSet = headDB.getAllHeads();
            if(headSet == null) return;
            while(headSet.next()) {
                Location loc = new Location(Bukkit.getWorld(headSet.getString("worldName")), headSet.getFloat("x"), headSet.getFloat("y"), headSet.getFloat("z"));
                UUID player = UUID.fromString(headSet.getString("player"));
                String name = headSet.getString("name");
                List<String> lore = new ArrayList<>();
                if(!Objects.equals(headSet.getString("lore0"), "null")) lore.add(headSet.getString("lore0"));
                if(!Objects.equals(headSet.getString("lore1"), "null")) lore.add(headSet.getString("lore1"));
                if(!Objects.equals(headSet.getString("lore2"), "null")) lore.add(headSet.getString("lore2"));
                if(!Objects.equals(headSet.getString("lore3"), "null")) lore.add(headSet.getString("lore3"));
                if(!Objects.equals(headSet.getString("lore4"), "null")) lore.add(headSet.getString("lore4"));
                Head head = new Head(loc, player, name, lore);
                this.heads.add(head);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesLocationHaveHead(Location location) {
        for(Head head : this.heads) {
            if(head.getLocation().equals(location)) return true;
        }
        return false;
    }

    public Head getHead(Location location) {
        for(Head head : this.heads) {
            if(head.getLocation().equals(location)) return head;
        }
        return null;
    }

    public void addHead(Head head) {
        heads.add(head);
        addHeadToDB(head);
    }

    public void removeHead(Head head) {
        heads.remove(head);
        removeHeadFromDB(head);
    }

    private void addHeadToDB(Head head) {
        headDB.addHead(head);
    }

    private void removeHeadFromDB(Head head) {
        headDB.removeHead(head);
    }
}
