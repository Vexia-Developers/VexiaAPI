package fr.vexia.api;

import fr.vexia.api.data.connectors.DatabaseConnection;
import fr.vexia.api.data.connectors.DatabaseConnectionBuilder;
import fr.vexia.api.data.redis.RedisConnection;

public class VexiaAPI {

    public void init() {
        DatabaseConnectionBuilder.aDatabaseConnection().withHost("play.vexia.fr")
                .withUser("postgres").withPassword("8YmADe9e7kBXqhw6").withPort(5432).withDatabase("minecraft")
                .build();

        new RedisConnection("play.vexia.fr", "s7CgJXDbf33LCnGM", 6300);
    }

    public void stop() {
        RedisConnection.close();
        DatabaseConnection.close();
    }

}
