package me.gustavo.ghsecundaryeconomy.commands.registry;

import lombok.RequiredArgsConstructor;
import me.gustavo.ghsecundaryeconomy.GHSecundaryEconomy;
import me.gustavo.ghsecundaryeconomy.commands.EconCommands;
import me.saiintbrisson.bukkit.command.BukkitFrame;

@RequiredArgsConstructor(staticName = "of")
public class CommandsRegistry {

    private final GHSecundaryEconomy plugin;

    public void register() {
        try {
            final BukkitFrame frame = new BukkitFrame(plugin);

            frame.registerCommands(new EconCommands(), plugin);

            plugin.getLogger().info("Os comandos foram registrados com sucesso!");
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getLogger().severe("Nao foi possível registrar os comandos.");
        }
    }
}
