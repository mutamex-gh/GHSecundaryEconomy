package me.gustavo.ghsecundaryeconomy;

import com.google.common.base.Stopwatch;
import lombok.val;
import me.gustavo.ghsecundaryeconomy.commands.registry.CommandsRegistry;
import me.gustavo.ghsecundaryeconomy.configuration.registry.ConfigRegistry;
import me.gustavo.ghsecundaryeconomy.database.manager.SQLManager;
import me.gustavo.ghsecundaryeconomy.database.SQLConnector;
import me.gustavo.ghsecundaryeconomy.listeners.CheckAndGive;
import me.gustavo.ghsecundaryeconomy.listeners.CreateDBListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class GHSecundaryEconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        val loadTime = Stopwatch.createStarted();

        SQLConnector.init(this);

        saveDefaultConfig();
        ConfigRegistry.register();

        try{
            SQLManager.of().createTable();
            getLogger().info("Conexao com o banco de dados inicializado com sucesso!");
        } catch (SQLException e) {
            getLogger().info("Nao foi possivel iniciar a conexao com o banco de dados!");
        }

        CommandsRegistry.of(this).register();
        loadListeners();

        loadTime.stop();
        getLogger().log(Level.INFO, "Plugin inicializado com sucesso. ({0})", loadTime);
    }

    @Override
    public void onDisable() {
        try{
            SQLManager.of().closeConnection();
            getLogger().info("Conexao com o banco de dados desabilitado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new CreateDBListener(), this);
        Bukkit.getPluginManager().registerEvents(new CheckAndGive(), this);
    }

    public static GHSecundaryEconomy getInstance() {
        return getPlugin(GHSecundaryEconomy.class);
    }
}
