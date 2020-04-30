package fr.vexia.api.data.connectors;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    DatabaseConnection(DatabaseConnectionBuilder builder) {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://" + builder.host + ":" + builder.port + "/" + builder.database);
        dataSource.setUsername(builder.user);
        dataSource.setPassword(builder.password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.addDataSourceProperty("serverTimezone", "Europe/Paris");

        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);
        dataSource.addDataSourceProperty("useLocalSessionState", true);
        dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
        dataSource.addDataSourceProperty("cacheResultSetMetadata", true);
        dataSource.addDataSourceProperty("cacheServerConfiguration", true);
        dataSource.addDataSourceProperty("elideSetAutoCommits", true);
        dataSource.addDataSourceProperty("maintainTimeStats", false);

        dataSource.setMaximumPoolSize(15);
    }

    public static Connection connection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {

    }
}