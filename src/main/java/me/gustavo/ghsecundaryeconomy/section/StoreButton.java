package me.gustavo.ghsecundaryeconomy.section;

import lombok.Getter;
import me.gustavo.ghsecundaryeconomy.api.APISection;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigStoreSection;
import me.gustavo.ghsecundaryeconomy.inventory.StoreInventory;
import me.gustavo.ghsecundaryeconomy.util.ColorUtil;
import me.gustavo.ghsecundaryeconomy.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StoreButton {

    @Getter public static final Inventory storeInventory = StoreInventory.getInventory();
    public static final Map<String, APISection> storeButton = new LinkedHashMap<>();

    static {
        ConfigurationSection itemSection = ConfigStoreSection.get(ConfigStoreSection::storeSection);

        for(String key : itemSection.getKeys(false)) {

            Material material = Material.valueOf(itemSection.getString(key + ".material"));
            String name = itemSection.getString(key + ".name");
            List<String> lore = itemSection.getStringList(key + ".lore");
            int slot = itemSection.getInt(key + ".slot");
            int price = itemSection.getInt(key + ".price");
            List<String> command = itemSection.getStringList(key + ".command");

            ItemBuilder itemBuilder = new ItemBuilder(material)
                    .changeItemMeta(itemMeta -> {
                        itemMeta.setDisplayName(ColorUtil.colored(name));
                        itemMeta.setLore(ColorUtil.colored(lore));
                    })
                    .setNBTTag("storeitems", key);

            storeButton.put(key, new APISection(key, itemBuilder.wrap(), slot, price, command));
        }

        storeButton.forEach((k, button) -> {
            if (button.getSlot() >= 0 && button.getSlot() < storeInventory.getSize()) {
                storeInventory.setItem(button.getSlot(), button.getMaterial());
            }
        });
    }

}
