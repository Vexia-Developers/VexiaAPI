package fr.vexia.api.data.manager;

import fr.vexia.api.data.executors.DatabaseExecutor;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.api.servers.GameState;
import fr.vexia.api.servers.GameType;
import fr.vexia.api.servers.ServerType;
import fr.vexia.api.servers.VexiaServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ServerManager {
    /*
CREATE TABLE servers (
  name VARCHAR(255) DEFAULT NULL,
  servertype VARCHAR(255) NOT NULL,
  gametype VARCHAR(255) DEFAULT NULL,
  state VARCHAR(255) NOT NULL,
  online int NOT NULL DEFAULT 0,
  max int NOT NULL DEFAULT 0,
  port int NOT NULL DEFAULT 0,
  PRIMARY KEY (name)
);
     */

    private static final String SAVE = "INSERT INTO servers(name, servertype, gametype, state, online, max, port) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (name) DO UPDATE SET "
            + "servertype = EXCLUDED.servertype, gametype = EXCLUDED.gametype, "
            + "state = EXCLUDED.state, online = EXCLUDED.online, max = EXCLUDED.max, "
            + "port = EXCLUDED.port";
    private static final String GET_BY_NAME = "SELECT * FROM servers WHERE name = ?";
    private static final String GET_BY_GAMETYPE = "SELECT * FROM servers WHERE gametype = ?";
    private static final String ONLINES_BY_GAMETYPE = "SELECT SUM(online) AS onlines FROM servers WHERE gametype = ?";
    private static final String DELETE_BY_NAME = "DELETE FROM servers WHERE name = ?";

    public static void save(VexiaServer server) {

        boolean exist = get(server.getName()) != null;

        DatabaseExecutor.executeVoidQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setString(1, server.getName());
            statement.setString(2, server.getServerType().name());
            statement.setString(3, server.getGameType() != null ? server.getGameType().name() : null);
            statement.setString(4, server.getState() != null ? server.getState().name() : null);
            statement.setInt(5, server.getOnline());
            statement.setInt(6, server.getMax());
            statement.setInt(7, server.getPort());
            statement.executeUpdate();

            if (!exist) {
                PubSubAPI.publish("serverupdater-create", server.getName());
            }

        });
    }

    public static VexiaServer get(String name) {
        return DatabaseExecutor.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(GET_BY_NAME);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return getVexiaServer(resultSet);
            }
            return null;
        });
    }

    public static void delete(VexiaServer server) {
        DatabaseExecutor.executeVoidQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_NAME);
            statement.setString(1, server.getName());

            statement.executeUpdate();
        });
    }

    public static int getOnlines(GameType gameType) {
        return DatabaseExecutor.executeQuery(connection -> {
            PreparedStatement statement;
            if(gameType == null) {
                statement = connection.prepareStatement(ONLINES_BY_GAMETYPE.replace("= ?", "IS NULL"));
            } else {
                statement = connection.prepareStatement(ONLINES_BY_GAMETYPE);
                statement.setString(1, gameType.name());
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -1;
        });
    }

    public static List<VexiaServer> getServers(GameType gameType) {
        return DatabaseExecutor.executeQuery(connection -> {
            List<VexiaServer> servers = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(GET_BY_GAMETYPE);
            statement.setString(1, gameType.name());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                servers.add(getVexiaServer(resultSet));
            }
            return servers;
        });
    }

    private static VexiaServer getVexiaServer(ResultSet resultSet) throws SQLException {

        String name = resultSet.getString("name").trim();
        ServerType serverType = ServerType.valueOf(resultSet.getString("servertype").trim());
        GameType gameType = resultSet.getString("gametype") != null ? GameType.valueOf(resultSet.getString("gametype").trim()) : null;
        GameState state = resultSet.getString("state") != null ? GameState.valueOf(resultSet.getString("state").trim()) : null;
        int max = resultSet.getInt("max");
        int port = resultSet.getInt("port");
        int online = resultSet.getInt("online");

        return new VexiaServer(name, serverType, gameType, state, online, max, port);
    }
}
