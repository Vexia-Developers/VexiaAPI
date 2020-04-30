package fr.vexia.api.data.connectors;

public class DatabaseConnectionBuilder {

    String host, user, password, database;
    int port;

    private DatabaseConnectionBuilder() {
    }

    public static DatabaseConnectionBuilder aDatabaseConnection() {
        return new DatabaseConnectionBuilder();
    }

    public DatabaseConnectionBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public DatabaseConnectionBuilder withUser(String user) {
        this.user = user;
        return this;
    }

    public DatabaseConnectionBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public DatabaseConnectionBuilder withDatabase(String database) {
        this.database = database;
        return this;
    }

    public DatabaseConnectionBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public DatabaseConnection build() {
        return new DatabaseConnection(this);
    }

}