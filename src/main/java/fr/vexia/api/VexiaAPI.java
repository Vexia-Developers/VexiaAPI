package fr.vexia.api;

import fr.vexia.api.data.connectors.DatabaseConnection;
import fr.vexia.api.data.connectors.DatabaseConnectionBuilder;
import fr.vexia.api.data.redis.RedisConnection;

public class VexiaAPI {

    public void init() {
        DatabaseConnectionBuilder.aDatabaseConnection().withHost("127.0.0.1")
                .withUser("postgres").withPassword("root").withPort(5432).withDatabase("vexia")
                .build();

        new RedisConnection("127.0.0.1", null, 6379);
    }

    public void stop() {
        RedisConnection.close();
        DatabaseConnection.close();
    }

}
