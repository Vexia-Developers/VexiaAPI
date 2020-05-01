package fr.vexia.api.data.manager;

import fr.vexia.api.data.executors.DatabaseExecutor;
import fr.vexia.api.players.VexiaPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendManager {

    /*
CREATE TABLE friends (
    player uuid,
    "anotherPlayer" uuid
);
     */

    private static final String TABLE_NAME = "friends";

    private static final String ADD_FRIENDSHIP = "INSERT INTO " + TABLE_NAME + "(player, anotherplayer) VALUES(?::uuid, ?::uuid)";
    private static final String DELETE_FRIENDSHIP = "DELETE FROM " + TABLE_NAME + " WHERE " +
            "(player = ?::uuid AND anotherplayer = ?::uuid)" +
            " OR " +
            "(player = ?:: AND anotherplayer = ?::uuid)";
    private static final String IS_FRIEND = "SELECT * FROM " + TABLE_NAME + " WHERE (player = ?::uuid AND anotherplayer = ?::uuid) OR (player = ?::uuid AND anotherplayer = ?::uuid)";
    private static final String GET_FRIENDS = "SELECT * FROM " + TABLE_NAME + " WHERE player = ?::uuid OR anotherplayer = ?::uuid";

    public static void create(UUID player, UUID anotherPlayer) {
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(ADD_FRIENDSHIP);
            statement.setObject(1, player);
            statement.setObject(2, anotherPlayer);
            statement.executeUpdate();
        });
    }

    public static void delete(UUID player, UUID anotherPlayer) {
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(DELETE_FRIENDSHIP);
            statement.setObject(1, player);
            statement.setObject(2, anotherPlayer);
            statement.executeUpdate();
        });
    }

    public static boolean areFriends(UUID player, UUID anotherPlayer) {
        return DatabaseExecutor.executeQuery(data -> {
            PreparedStatement statement = data.prepareStatement(IS_FRIEND);
            statement.setObject(1, player);
            statement.setObject(2, anotherPlayer);

            statement.setObject(4, player);
            statement.setObject(3, anotherPlayer);
            return statement.executeQuery().next();
        });
    }

    public static List<VexiaPlayer> getFriends(UUID player) {
        List<VexiaPlayer> friends = new ArrayList<>();
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(GET_FRIENDS);
            statement.setObject(1, player);
            statement.setObject(2, player);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID friend = UUID.fromString(rs.getString(1));
                if (friend.equals(player)) {
                    friend = UUID.fromString(rs.getString(2));
                }
                friends.add(PlayerManager.get(friend));
            }
        });
        return friends;
    }

}
