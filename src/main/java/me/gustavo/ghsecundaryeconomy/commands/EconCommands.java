package me.gustavo.ghsecundaryeconomy.commands;

import lombok.val;
import me.gustavo.ghsecundaryeconomy.configuration.ConfigMessages;
import me.gustavo.ghsecundaryeconomy.database.manager.SQLManager;
import me.gustavo.ghsecundaryeconomy.section.StoreButton;
import me.gustavo.ghsecundaryeconomy.util.ColorUtil;
import me.gustavo.ghsecundaryeconomy.util.Util;
import me.gustavo.ghsecundaryeconomy.util.Values;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class EconCommands {

    private final SQLManager sqlManager = SQLManager.of();

    @Command(
            name = "gold",
            permission = "ghse.commands.gold",
            usage = "&cComando correto: /gold <player>",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void viewBalance(Context<Player> context, @Optional OfflinePlayer target) throws SQLException {
        val sender = context.getSender();
        val playerBalance = sqlManager.getBalance(sender.getName());

        if (target == null) {

            sender.sendMessage(ConfigMessages.get(ConfigMessages::balance)
                    .replace("{balance}", Values.format(playerBalance)));

        } else if (target.hasPlayedBefore()) {
            val targetBalance = sqlManager.getBalance(target.getName());
            sender.sendMessage(ConfigMessages.get(ConfigMessages::targetBalance)
                    .replace("{balance}", Values.format(targetBalance))
                    .replace("{player}", target.getName()));
        } else {
            sender.sendMessage(ConfigMessages.get(ConfigMessages::incorrectUsage));
        }
    }

    @Command(
            name = "gold.set",
            permission = "ghse.commands.admin",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void setPlayerBalance(Context<Player> context, Player target, int amount) throws SQLException {
        val sender = context.getSender();

        if (target != null && amount >= 0) {
            sqlManager.setBalance(target.getName(), amount);
            sender.sendMessage(ConfigMessages.get(ConfigMessages::setBalance)
                    .replace("{amount}", Values.format(amount))
                    .replace("{player}", target.getName()));
        } else {
            sender.sendMessage(ConfigMessages.get(ConfigMessages::incorrectUsage));
        }
    }

    @Command(
            name = "gold.pay",
            aliases = {"enviar, send"},
            permission = "ghse.commands.pay",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void payCommand(Context<Player> context, Player target, int amount) throws SQLException {
        val sender = context.getSender();
        val balance = sqlManager.getBalance(sender.getName());

        if (target != null && amount > 0) {
            if(target == sender) return;

            if (balance >= amount) {

                sqlManager.withdrawPlayer(sender.getName(), amount);
                sqlManager.depositPlayer(target.getName(), amount);

                sender.sendMessage(ConfigMessages.get(ConfigMessages::payMessage)
                        .replace("{amount}", Values.format(amount))
                        .replace("{player}", sender.getName()));

                target.sendMessage(ConfigMessages.get(ConfigMessages::receiveMessage)
                        .replace("{amount}", Values.format(amount))
                        .replace("{player}", sender.getName()));

            } else {
                sender.sendMessage(ConfigMessages.get(ConfigMessages::noGold));
            }
        } else {
            sender.sendMessage(ConfigMessages.get(ConfigMessages::incorrectUsage));
        }
    }

    @Command(
            name = "gold.top",
            permission = "ghse.commands.top",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void goldTop(Context<Player> context) throws SQLException {
        val player = context.getSender();

        List<String> topPlayers = sqlManager.getTopPlayers();

        for(String list : topPlayers) {
            player.sendMessage(ColorUtil.colored("&e-------------------------------"));
            player.sendMessage(ColorUtil.colored("&e&l              GOLD            "));
            player.sendMessage(ColorUtil.colored("&f    Top 10 jogadores mais ricos"));
            player.sendMessage(list);
            player.sendMessage(ColorUtil.colored("&e-------------------------------"));
        }
    }

    @Command(
            name = "gold.store",
            permission = "ghse.commands.store",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void goldStore(Context<Player> context) {
        val player = context.getSender();

        Util.sendSound(player, Sound.CLICK);
        player.openInventory(StoreButton.getStoreInventory());
    }
}
