package fr.vexia.api.data.manager;

import fr.vexia.api.data.executors.DatabaseExecutor;
import fr.vexia.api.servers.GameType;
import fr.vexia.api.stats.Statistic;
import fr.vexia.api.stats.StatsType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

public class StatsManager {

    public static final String STATS_PREFIX = "stats_";
    public static final String GET_BY_GAMETYPE = "SELECT * FROM "+STATS_PREFIX+"%s WHERE uuid = ?";

    private static HashMap<GameType, String> requests = new HashMap<>();
    static {
        for (GameType gameType : GameType.values()) {
            requests.put(gameType, buildSQLRequest(gameType));
        }
    }

    public static void saveStatistic(UUID uuid, Statistic statistic) {
        DatabaseExecutor.executeVoidQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(requests.get(statistic.getGameType()));
            statement.setObject(1, uuid);

            StatsType[] statsTypes = statistic.getGameType().getStatsTypes();
            for (int i = 0; i < statsTypes.length; i++) {
                statement.setInt(i+2, statistic.get(statsTypes[i]));
            }

            statement.executeUpdate();
        });
    }

    public static Statistic getStatistic(UUID uuid, GameType gameType) {
        return DatabaseExecutor.executeQuery(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(GET_BY_GAMETYPE, gameType.name().toLowerCase()));
            preparedStatement.setObject(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            HashMap<StatsType, Integer> stats = new HashMap<>();
            for (StatsType statsType : gameType.getStatsTypes()) {
                stats.put(statsType, resultSet.getInt(statsType.name().toLowerCase()));
            }
            return new Statistic(gameType, stats);
        });
    }

    private static String buildSQLRequest(GameType gameType) {
        StatsType[] statsTypes = gameType.getStatsTypes();
        StringBuilder sql = new StringBuilder("INSERT INTO stats_");
        sql.append(gameType.name().toLowerCase()).append(" (uuid,");

        StringBuilder values = new StringBuilder(") VALUES(?::uuid");
        StringBuilder conflict = new StringBuilder("ON CONFLICT (\"uuid\") DO UPDATE SET ");

        for (int i = 0; i < statsTypes.length; i++) {
            String column = statsTypes[i].name().toLowerCase();
            sql.append(column);
            conflict.append(column).append(" = ").append("EXCLUDED.").append(column);
            if (i != statsTypes.length - 1) {
                sql.append(",");
                conflict.append(",");
            }
            values.append(",?");
        }

        sql.append(values.toString());
        sql.append(")");
        return sql.toString();
    }

}
