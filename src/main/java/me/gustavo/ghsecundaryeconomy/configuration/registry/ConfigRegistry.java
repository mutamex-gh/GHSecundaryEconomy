package me.gustavo.ghsecundaryeconomy.configuration.registry;

import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import me.gustavo.ghsecundaryeconomy.GHSecundaryEconomy;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigDatabase;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigMessages;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigStoreSection;

public class ConfigRegistry {

    public static void register() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(GHSecundaryEconomy.getInstance());

        configurationInjector.saveDefaultConfiguration(
                GHSecundaryEconomy.getInstance(),
                "config.yml",
                "store.yml"
        );

        configurationInjector.injectConfiguration(
                ConfigDatabase.instance(),
                ConfigMessages.instance(),
                ConfigStoreSection.instance()
        );
    }
}
