package me.gustavo.ghsecundaryeconomy.listeners;

import lombok.val;
import me.gustavo.ghsecundaryeconomy.api.APISection;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigMessages;
import me.gustavo.ghsecundaryeconomy.database.manager.SQLManager;
import me.gustavo.ghsecundaryeconomy.inventory.StoreInventory;
import me.gustavo.ghsecundaryeconomy.section.StoreButton;
import me.gustavo.ghsecundaryeconomy.util.ItemBuilder;
import me.gustavo.ghsecundaryeconomy.util.Util;
import me.gustavo.ghsecundaryeconomy.util.Values;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class CheckAndGive implements Listener {

    private final SQLManager sqlManager = SQLManager.of();

    @EventHandler
    public void clickToBuy(InventoryClickEvent event) throws SQLException {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null) item = event.getCursor();
        if (item == null || item.getType() == Material.AIR) return;

        String nbtTag = ItemBuilder.getNBTTag(item, "storeitems");
        if (StoreButton.storeButton.get(nbtTag) == null) return;
        APISection button = StoreButton.storeButton.get(nbtTag);

        if(event.getClick().isLeftClick() || event.getClick().isRightClick()) {
            int balance = sqlManager.getBalance(player.getName());

            if(balance >= button.getPrice()) {
                sqlManager.withdrawPlayer(player.getName(), button.getPrice());

                Util.sendSound(player, Sound.LEVEL_UP);
                player.closeInventory();

                for(String command : button.getCommand()) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command.replace("{player}", player.getName())
                    );
                }

                player.sendMessage(ConfigMessages.get(ConfigMessages::successBuy)
                        .replace("{gold}", Values.format(button.getPrice())));
            }else {
                player.sendMessage(ConfigMessages.get(ConfigMessages::noGoldToBuy));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void cancelMovements(InventoryClickEvent event) {
        val inventoryName = StoreInventory.getInventoryTitle();

        if(event.getInventory().getTitle().equalsIgnoreCase(inventoryName)) {
            event.setCancelled(true);
        }
    }

}
