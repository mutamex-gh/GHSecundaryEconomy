package me.gustavo.ghsecundaryeconomy.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Util {

    public static void sendSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }

}
