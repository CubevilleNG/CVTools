package org.cubeville.cvtools.heads;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubeville.cvtools.CVTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.Objects;

public class HeadDB extends HeadSQL {

    public HeadDB(CVTools plugin) {
        super(plugin);
    }

    public String SQLiteCreateHeadsTable = "CREATE TABLE IF NOT EXISTS heads (" +
            "`headID` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`player` varchar(64) NOT NULL," +
            "`name` varchar(255) NOT NULL," +
            "`lore0` varchar(255)," +
            "`lore1` varchar(255)," +
            "`lore2` varchar(255)," +
            "`lore3` varchar(255)," +
            "`lore4` varchar(255)," +
            "`worldName` varchar(64) NOT NULL," +
            "`x` BIGINT NOT NULL," +
            "`y` BIGINT NOT NULL," +
            "`z` BIGINT NOT NULL" +
            ");";

    public void load() {
        connect();
        update(SQLiteCreateHeadsTable);
    }

    public ResultSet getAllHeads() {
        return getResult("SELECT * FROM `heads`");
    }

    public void createBackup(JavaPlugin plugin) throws IOException {
        File dbFile = new File(plugin.getDataFolder(), "heads.db");
        if(dbFile.exists()) {
            Path source = dbFile.toPath();
            Path target = plugin.getDataFolder().toPath();
            Files.copy(source, target.resolve("heads-backup.db"), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void addHead(Head head) {
        Location loc = head.getLocation();
        String lore0 = (head.getLore() != null && head.getLore().size() > 0) ? head.getLore().get(0) : null;
        String lore1 = (head.getLore() != null && head.getLore().size() > 1) ? head.getLore().get(1) : null;
        String lore2 = (head.getLore() != null && head.getLore().size() > 2) ? head.getLore().get(2) : null;
        String lore3 = (head.getLore() != null && head.getLore().size() > 3) ? head.getLore().get(3) : null;
        String lore4 = (head.getLore() != null && head.getLore().size() > 4) ? head.getLore().get(4) : null;
        update("INSERT INTO `heads` (player, name, lore0, lore1, lore2, lore3, lore4, worldName, x, y, z) " +
                "VALUES(\"" + head.getPlayer().toString() + "\", \"" + head.getName() + "\", \"" + lore0 + "\", \"" + lore1 + "\", \"" + lore2 + "\", \"" + lore3 + "\", \"" + lore4 + "\", \"" + Objects.requireNonNull(loc.getWorld()).getName() + "\", \"" + loc.getX() + "\", \"" + loc.getY() + "\", \"" + loc.getZ() + "\");"
        );
    }

    public void removeHead(Head head) {
        Location loc = head.getLocation();
        update("DELETE FROM `heads`" +
                " WHERE worldName = \"" + Objects.requireNonNull(loc.getWorld()).getName() + "\"" +
                " AND x = \"" + loc.getX() + "\"" +
                " AND y = \"" + loc.getY() + "\"" +
                " AND z = \"" + loc.getZ() + "\";"
        );
    }
}
