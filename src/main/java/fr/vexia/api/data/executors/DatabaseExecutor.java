package fr.vexia.api.data.executors;

import fr.vexia.api.data.connectors.DatabaseConnection;
import java.sql.Connection;

/**
 * Class to execute SQL request
 */
public final class DatabaseExecutor {

    private DatabaseExecutor() {
    }

    /**
     * Execute a SQL query that return a value
     *
     * @param executor Executor
     * @param <T>      Type of value desired
     * @return Instance of type desired obtained by the request
     */
    public static <T> T executeQuery(QueryExecutor<T> executor) {
        T value = null;
        try (Connection connection = DatabaseConnection.connection()) {
            value = executor.perform(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void executeVoidQuery(QueryVoidExecutor executor) {
        try (Connection connection = DatabaseConnection.connection()) {
            executor.perform(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

