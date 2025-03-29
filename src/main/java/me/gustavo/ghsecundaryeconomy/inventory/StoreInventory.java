package me.gustavo.ghsecundaryeconomy.inventory;

import me.gustavo.ghsecundaryeconomy.configuration.ConfigInventory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class StoreInventory {

    private static final String inventoryName = ConfigInventory.get(ConfigInventory::inventoryName);

    public static Inventory getInventory() {
        return Bukkit.createInventory(
                null,
                ConfigInventory.get(ConfigInventory::inventorySlot),
                ConfigInventory.get(ConfigInventory::inventoryName)
        );
    }

    public static String getInventoryTitle() {
        return inventoryName;
    }
}
