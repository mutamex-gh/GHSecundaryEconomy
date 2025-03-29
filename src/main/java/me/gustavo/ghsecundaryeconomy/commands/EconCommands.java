package me.gustavo.ghsecundaryeconomy.commands;

import lombok.val;
import me.gustavo.ghsecundaryeconomy.GHSecundaryEconomy;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class EconCommands {

    private final SQLManager sqlManager = SQLManager.of();

    @Command(
            name = "gold",
            permission = "ghse.commands.gold",
            description = "Visualize o seu saldo gold ou o de algum jogador.",
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
            description = "Seta a quantia de gold para um jogador.",
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
            description = "Transfere gold para o jogador selecionado.",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void payCommand(Context<Player> context, Player target, int amount) throws SQLException {
        val sender = context.getSender();
        val balance = sqlManager.getBalance(sender.getName());

        if (sqlManager.getReceipt(target.getName()) == 0) {
            sender.sendMessage(ConfigMessages.get(ConfigMessages::receiptOff)
                    .replace("{player}", target.getName()));
            return;
        }

        if (target != null && amount > 0) {
            if (target == sender) {
                sender.sendMessage(ConfigMessages.get(ConfigMessages::invalidTarget));
                return;
            }

            if (balance >= amount) {

                sqlManager.withdrawPlayer(sender.getName(), amount);
                sqlManager.depositPlayer(target.getName(), amount);

                sender.sendMessage(ConfigMessages.get(ConfigMessages::payMessage)
                        .replace("{amount}", Values.format(amount))
                        .replace("{player}", target.getName()));

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
            description = "Veja os jogadores com mais gold do servidor.",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void goldTop(Context<Player> context) throws SQLException {
        val player = context.getSender();

        List<String> topPlayers = sqlManager.getTopPlayers();

        player.sendMessage(ColorUtil.colored("&e-------------------------------"));
        player.sendMessage(ColorUtil.colored("&e&l              GOLD            "));
        player.sendMessage(ColorUtil.colored("&f    Top 10 jogadores mais ricos"));
        for (String list : topPlayers) {
            player.sendMessage(ColorUtil.colored(list));
        }
        player.sendMessage(ColorUtil.colored("&e-------------------------------"));
    }

    @Command(
            name = "gold.store",
            aliases = {"menu", "shop", "loja"},
            permission = "ghse.commands.store",
            description = "Abre o menu da loja de gold.",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void goldStore(Context<Player> context) {
        val player = context.getSender();

        Util.sendSound(player, Sound.CLICK);
        player.openInventory(StoreButton.getStoreInventory());
    }

    @Command(
            name = "gold.toggle",
            aliases = {"recebimento", "receber", "receipt"},
            permission = "ghse.commands.receipt",
            description = "Ativa/desativa o recebimento de gold.",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void setReceipt(Context<CommandSender> context) throws SQLException {
        val sender = context.getSender();

        if (sqlManager.getReceipt(sender.getName()) == 1) {
            sqlManager.setReceipt(sender.getName(), 0);
            sender.sendMessage(ConfigMessages.get(ConfigMessages::changeModeToOff));
        } else {
            sqlManager.setReceipt(sender.getName(), 1);
            sender.sendMessage(ConfigMessages.get(ConfigMessages::changeModeToOn));
        }
    }

    @Command(
            name = "gold.help",
            aliases = {"ajuda"},
            description = "Lista todos os comandos do plugin.",
            async = true
    )
    public void help(Context<CommandSender> context) {
        val sender = context.getSender();

        List<String> help = GHSecundaryEconomy.getInstance().getConfig().getStringList("messages.help");
        List<String> helpToAdmin = GHSecundaryEconomy.getInstance().getConfig().getStringList("messages.helpToAdmin");

        for (val basicHelp : help) {
            sender.sendMessage(ColorUtil.colored(basicHelp));
            if (sender.hasPermission("ghse.commands.admin")) {
                for (val adminHelp : helpToAdmin) {
                    sender.sendMessage(ColorUtil.colored(adminHelp));
                }
            }
        }
    }

}
