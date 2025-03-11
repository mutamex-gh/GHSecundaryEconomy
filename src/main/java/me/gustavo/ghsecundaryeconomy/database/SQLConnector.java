package me.gustavo.ghsecundaryeconomy.database;

import me.gustavo.ghsecundaryeconomy.GHSecundaryEconomy;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnector {

    private static GHSecundaryEconomy plugin;

    public static void init(GHSecundaryEconomy pluginInstance) {
        plugin = pluginInstance;
    }

    public static Connection connect() {
        String sqlType = ConfigDatabase.get(ConfigDatabase::TYPE);

        String ADDRESS = ConfigDatabase.get(ConfigDatabase::ADDRESS);
        String DATABASE = ConfigDatabase.get(ConfigDatabase::DATABASE);

        if (sqlType.equalsIgnoreCase("mysql")) {
            String URL = "jdbc:mysql://" + ADDRESS + "/" + DATABASE + "?characterEncoding=UTF-8&useSSL=false";

            try {
                return DriverManager.getConnection(
                        URL,
                        ConfigDatabase.get(ConfigDatabase::USERNAME),
                        ConfigDatabase.get(ConfigDatabase::PASSWORD)
                );
            } catch (SQLException s) {
                s.printStackTrace();
            }
        } else if (sqlType.equalsIgnoreCase("sqlite")) {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            try {
                File database = new File(plugin.getDataFolder(), "database.db");
                String URL = "jdbc:sqlite:" + database.getAbsolutePath();
                return DriverManager.getConnection(URL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
