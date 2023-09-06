package org.cubeville.cvtools.heads;

import org.cubeville.cvtools.CVTools;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class HeadSQL {

    private final CVTools plugin;
    private Connection connection;
    private Statement statement;

    public HeadSQL(CVTools plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        connection = null;
        try {
            File dbFile = new File(plugin.getDataFolder(), "heads.db");
            if(!dbFile.exists()) {
                dbFile.createNewFile();
            }
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
