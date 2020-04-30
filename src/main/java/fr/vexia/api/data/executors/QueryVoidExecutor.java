package fr.vexia.api.data.executors;

import java.sql.Connection;

/**
 *SQL query executor without result
 */
public interface QueryVoidExecutor {
    /**
     * Perform a SQL query with a connection
     * @param connection Connection JDBC
     * @throws Exception Exception during the request
     */
    void perform(Connection connection) throws Exception;
}