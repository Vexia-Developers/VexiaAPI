package fr.vexia.api.data.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.vexia.api.data.executors.DatabaseExecutor;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.options.Option;
import fr.vexia.api.players.rank.Rank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class PlayerManager {

    /*
CREATE TABLE accounts (
  uuid uuid NOT NULL,
  name varchar(50) DEFAULT NULL,
  ip char(15) DEFAULT NULL,
  rank varchar(50) NOT NULL DEFAULT 'JOUEUR',
  rankexpires timestamp with time zone DEFAULT NULL,
  coins int NOT NULL DEFAULT 0,
  credits int NOT NULL DEFAULT 0,
  firstjoin timestamp with time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  lastjoin timestamp with time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  server varchar(50) DEFAULT NULL,
  options jsonb NOT NULL DEFAULT '{}',
  PRIMARY KEY (uuid)
);
     */

    private static final String TABLE_NAME = "accounts";

    private static final String GET_BY_UUID = "SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?::uuid";
    private static final String GET_BY_NAME = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";

    private static final String GETS_BY_IP = "SELECT * FROM " + TABLE_NAME + " WHERE ip = ?";
    private static final String GETS_BY_SERVER = "SELECT * FROM " + TABLE_NAME + " WHERE server = ?";
    private static final String GETS_ONLINE = "SELECT * FROM " + TABLE_NAME + " WHERE server IS NOT NULL";

    private static final String SAVE = "INSERT INTO " + TABLE_NAME + "(\"uuid\", name, ip, rank, rankexpires, coins, credits,"
            + "firstjoin, lastjoin, server, options) "
            + "VALUES (?::uuid, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb) ON CONFLICT (\"uuid\") DO UPDATE SET "
            + "name = EXCLUDED.name, ip = EXCLUDED.ip, rank = EXCLUDED.rank,"
            + "rankexpires = EXCLUDED.rankexpires, coins = EXCLUDED.coins,"
            + "credits = EXCLUDED.credits, lastjoin = EXCLUDED.lastjoin,"
            + "server = EXCLUDED.server, options = EXCLUDED.options";

    public static VexiaPlayer get(UUID uuid) {
        return DatabaseExecutor.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(GET_BY_UUID);
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            return rs.next() ? getVexiaPlayer(rs) : null;
        });
    }

    public static VexiaPlayer get(String name) {
        return DatabaseExecutor.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(GET_BY_NAME);
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            return rs.next() ? getVexiaPlayer(rs) : null;
        });
    }

    public static List<VexiaPlayer> getWithIP(String ip) {
        List<VexiaPlayer> players = new ArrayList<>();
        DatabaseExecutor.executeVoidQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(GETS_BY_IP);
            statement.setString(1, ip);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                players.add(getVexiaPlayer(rs));
            }
        });
        return players;
    }

    public static List<VexiaPlayer> getWithServer(String server) {
        List<VexiaPlayer> players = new ArrayList<VexiaPlayer>();
        DatabaseExecutor.executeVoidQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(GETS_BY_SERVER);
            statement.setString(1, server);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                players.add(getVexiaPlayer(rs));
            }
        });
        return players;
    }

    public static List<VexiaPlayer> getOnlines() {
        List<VexiaPlayer> players = new ArrayList<VexiaPlayer>();
        DatabaseExecutor.executeVoidQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(GETS_ONLINE);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                players.add(getVexiaPlayer(rs));
            }
        });
        return players;
    }

    public static void save(VexiaPlayer vexiaPlayer) {
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(SAVE);

            statement.setObject(1, vexiaPlayer.getUUID());
            statement.setString(2, vexiaPlayer.getName());
            statement.setString(3, vexiaPlayer.getIP());
            statement.setString(4, vexiaPlayer.getRank().name());
            statement.setTimestamp(5, vexiaPlayer.getRankExpires() != null ? new Timestamp(vexiaPlayer.getRankExpires().getTime()) : null);
            statement.setInt(6, vexiaPlayer.getCoins());
            statement.setInt(7, vexiaPlayer.getCredits());
            statement.setTimestamp(8, new Timestamp(vexiaPlayer.getFirstJoin().getTime()));
            statement.setTimestamp(9, new Timestamp(vexiaPlayer.getLastJoin().getTime()));
            statement.setString(10, vexiaPlayer.getServer());
            statement.setString(11, new Gson().toJson(vexiaPlayer.getOptions()));

            statement.executeUpdate();
        });
    }

    private static VexiaPlayer getVexiaPlayer(ResultSet resultSet) throws SQLException {
        UUID uuid = UUID.fromString(resultSet.getString("uuid").trim());
        String name = resultSet.getString("name").trim();
        String ip = resultSet.getString("ip").trim();
        Rank rank = Rank.valueOf(resultSet.getString("rank").trim());
        int coins = resultSet.getInt("coins");
        int credits = resultSet.getInt("credits");
        Date firstJoin = resultSet.getTimestamp("firstJoin");
        Date lastJoin = resultSet.getTimestamp("lastJoin");
        Date rankExpires = resultSet.getTimestamp("rankExpires");
        String server = resultSet.getString("server") != null ? resultSet.getString("server").trim() : null;
        HashMap<Option, Option.OptionValue> options = new Gson().fromJson(resultSet.getString("options"), new TypeToken<HashMap<Option, Option.OptionValue>>(){}.getType());

        return new VexiaPlayer(uuid, name, ip, rank, coins, credits, firstJoin, lastJoin, rankExpires, server, options);
    }
}