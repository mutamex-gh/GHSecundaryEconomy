package me.gustavo.ghsecundaryeconomy.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigFile("store.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigStoreSection implements ConfigurationInjectable {

    @Getter private static final ConfigStoreSection instance = new ConfigStoreSection();

    @ConfigField("store-items") private ConfigurationSection storeSection;

    public static <T> T get(Function<ConfigStoreSection, T> function) {
        return function.apply(instance);
    }
}
