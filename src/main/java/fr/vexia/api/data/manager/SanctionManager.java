package fr.vexia.api.data.manager;

import fr.vexia.api.data.executors.DatabaseExecutor;
import fr.vexia.api.sanctions.VexiaSanction;
import fr.vexia.api.sanctions.SanctionType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SanctionManager {

    /*
CREATE TABLE infractions (
  uuid uuid DEFAULT NULL,
  player uuid DEFAULT NULL,
  moderator uuid DEFAULT NULL,
  type VARCHAR(255) NOT NULL, CHECK (type IN ('BAN','MUTE','KICK')),
  reason varchar(255) DEFAULT 'Aucune raison',
  start timestamp with time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  finish timestamp with time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
  finished SMALLINT DEFAULT 0,
  PRIMARY KEY (uuid)
);
     */
    private static final String TABLE_NAME = "infractions";

    private static final String SAVE = "INSERT INTO " + TABLE_NAME + "(uuid, player, moderator, type, reason, start, finish, finished)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" +
            "ON CONFLICT (uuid) DO UPDATE SET finish = EXCLUDED.finish, finished = EXCLUDED.finished";
    private static final String GET_ACTIVE = "SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?::uuid AND (\"finish\" IS NULL OR \"finish\" < NOW())";


    public static void save(VexiaSanction sanction) {
        DatabaseExecutor.executeVoidQuery(c -> {
            PreparedStatement statement = c.prepareStatement(SAVE);
            statement.setObject(1, sanction.getID());
            statement.setObject(2, sanction.getPlayer());
            statement.setObject(3, sanction.getModerator());
            statement.setString(4, sanction.getSanctionType().name());
            statement.setString(5, sanction.getReason());
            statement.setTimestamp(6, new Timestamp(sanction.getStart().getTime()));
            statement.setTimestamp(7, sanction.getEnd() != null ? new Timestamp(sanction.getEnd().getTime()) : null);
            statement.setBoolean(8, sanction.isFinished());
            statement.executeUpdate();
        });
    }

    public static VexiaSanction getActiveSanction(UUID uuid, SanctionType type) {
        return DatabaseExecutor.executeQuery(c -> {
            PreparedStatement statement = c.prepareStatement(GET_ACTIVE + " AND type = ?");
            statement.setObject(1, uuid);
            statement.setString(2, type.name());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return build(rs);
            }
            return null;
        });
    }

    public static List<VexiaSanction> getAll(UUID uuid, SanctionType type) {
        return DatabaseExecutor.executeQuery(c -> {
            PreparedStatement statement = c.prepareStatement(GET_ACTIVE + " AND type = ? ORDER BY start DESC");
            statement.setString(1, uuid.toString());
            statement.setString(2, type.name());

            ResultSet rs = statement.executeQuery();
            List<VexiaSanction> sanctions = new ArrayList<>();
            while (rs.next()) {
                sanctions.add(build(rs));
            }
            return sanctions;
        });
    }

    public static List<VexiaSanction> getAll(UUID uuid) {
        return DatabaseExecutor.executeQuery(c -> {

            PreparedStatement statement = c.prepareStatement(GET_ACTIVE + " ORDER BY start DESC");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            List<VexiaSanction> sanctions = new ArrayList<>();
            while (rs.next()) {
                sanctions.add(build(rs));
            }
            return sanctions;
        });
    }

    private static VexiaSanction build(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("uuid"));
        UUID player = UUID.fromString(resultSet.getString("player"));
        UUID moderator = UUID.fromString(resultSet.getString("moderator"));
        SanctionType type = SanctionType.valueOf(resultSet.getString("type"));
        String reason = resultSet.getString("reason");
        Date start = new Date(resultSet.getTimestamp("start").getTime());
        Date end = resultSet.getTimestamp("finish") != null ?
                new Date(resultSet.getTimestamp("finish").getTime()) : null;
        boolean finished = resultSet.getInt("finished") == 1;

        return new VexiaSanction(id, player, moderator, type, reason, start, end, finished);
    }
}
