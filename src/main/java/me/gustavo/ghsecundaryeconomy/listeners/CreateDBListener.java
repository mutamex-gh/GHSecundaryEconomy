package me.gustavo.ghsecundaryeconomy.listeners;

import me.gustavo.ghsecundaryeconomy.database.manager.SQLManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class CreateDBListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        SQLManager sqlManager = SQLManager.of();
        Player player = event.getPlayer();

        try {
            sqlManager.createPlayerOnDB(player);
        } catch (SQLException ignore) {}
    }
}
