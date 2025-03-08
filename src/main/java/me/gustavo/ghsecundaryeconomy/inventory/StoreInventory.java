package me.gustavo.ghsecundaryeconomy.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class StoreInventory {

    private static final String inventoryName = "Gold Store";

    public static Inventory getInventory() {
        return Bukkit.createInventory(
                null,
                27,
                inventoryName
        );
    }

    public static String getInventoryTitle() {
        return inventoryName;
    }
}
