package me.gustavo.ghsecundaryeconomy.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigSection("database")
@ConfigFile("config.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigDatabase implements ConfigurationInjectable {

    @Getter private static final ConfigDatabase instance = new ConfigDatabase();

    //DB TYPE
    @ConfigField("type") private String TYPE;
    //PURE MYSQL
    @ConfigField("mysql.address") private String ADDRESS;
    @ConfigField("mysql.username") private String USERNAME;
    @ConfigField("mysql.password") private String PASSWORD;
    @ConfigField("mysql.database") private String DATABASE;

    public static <T> T get(Function<ConfigDatabase, T> function) {
        return function.apply(instance);
    }

}
