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

import java.util.List;
import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigSection("messages")
@ConfigFile("config.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigMessages implements ConfigurationInjectable {

    @Getter private static final ConfigMessages instance = new ConfigMessages();

    @ConfigField("balance") private String balance;
    @ConfigField("set-balance") private String setBalance;
    @ConfigField("target-balance") private String targetBalance;
    @ConfigField("incorrect-usage") private String incorrectUsage;
    @ConfigField("pay") private String payMessage;
    @ConfigField("receive") private String receiveMessage;
    @ConfigField("no-gold") private String noGold;
    @ConfigField("success-buy") private String successBuy;
    @ConfigField("no-gold-to-buy") private String noGoldToBuy;
    @ConfigField("change-mode-off") private String changeModeToOff;
    @ConfigField("change-mode-on") private String changeModeToOn;
    @ConfigField("receipt-off") private String receiptOff;
    @ConfigField("invalid-target") private String invalidTarget;

    public static <T> T get(Function<ConfigMessages, T> function) {
        return function.apply(instance);
    }
}
