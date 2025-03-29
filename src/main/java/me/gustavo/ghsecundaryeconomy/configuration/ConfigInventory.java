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
@ConfigSection("store-inventory")
@ConfigFile("store.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigInventory implements ConfigurationInjectable {

    @Getter private static final ConfigInventory instance = new ConfigInventory();

    @ConfigField("inventory-name") private String inventoryName;
    @ConfigField("inventory-slot") private Integer inventorySlot;

    public static <T> T get(Function<ConfigInventory, T> function) {
        return function.apply(instance);
    }

}

