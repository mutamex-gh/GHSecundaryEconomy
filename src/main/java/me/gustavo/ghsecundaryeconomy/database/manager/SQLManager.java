package me.gustavo.ghsecundaryeconomy.database.manager;

import lombok.RequiredArgsConstructor;
import me.gustavo.ghsecundaryeconomy.database.SQLConnector;
import me.gustavo.ghsecundaryeconomy.util.ColorUtil;
import me.gustavo.ghsecundaryeconomy.util.Values;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
public class SQLManager {

    final Connection connection = SQLConnector.connect();

    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS economy(" +
                            "uuid CHAR(36) PRIMARY KEY," +
                            "username TEXT NOT NULL," +
                            "amount INTEGER NOT NULL DEFAULT 0)"
            );
        }
    }

    public void setBalance(String player, Integer quantity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE economy SET amount = ? WHERE username = ?")) {

            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, player);

            preparedStatement.executeUpdate();
        }
    }

    public int getBalance(String player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT amount FROM economy WHERE username = ?")) {

            preparedStatement.setString(1, player);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("amount");
                } else {
                    return 0;
                }
            }
        }
    }

    public void depositPlayer(String player, Integer quantity) throws SQLException {

        int oldBalance = getBalance(player);
        int newBalance = oldBalance + quantity;

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE economy SET amount = ? WHERE username = ?")) {

            preparedStatement.setInt(1, newBalance);
            preparedStatement.setString(2, player);

            preparedStatement.executeUpdate();
        }
    }

    public void withdrawPlayer(String player, Integer quantity) throws SQLException {

        int oldBalance = getBalance(player);
        int newBalance = oldBalance - quantity;

        if(quantity > oldBalance) return;

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE economy SET amount = ? WHERE username = ?")) {

            preparedStatement.setInt(1, newBalance);
            preparedStatement.setString(2, player);

            preparedStatement.executeUpdate();
        }
    }

    private boolean playerExists(String username) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT 1 FROM economy WHERE username = ?")) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void createPlayerOnDB(Player player) throws SQLException {
        if (playerExists(player.getUniqueId().toString())) {
            return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO economy (uuid, username, amount) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setInt(3, 0);
            preparedStatement.executeUpdate();
        }
    }

    public List<String> getTopPlayers() throws SQLException {
        List<String> topPlayers = new ArrayList<>();

        String query = "SELECT username, amount FROM economy ORDER BY amount DESC LIMIT 10";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int amount = resultSet.getInt("amount");

                topPlayers.add(ColorUtil.colored("&e" + username + "&8 - &f" + Values.format(amount)));
            }
        }

        return topPlayers;
    }

    public void closeConnection() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
