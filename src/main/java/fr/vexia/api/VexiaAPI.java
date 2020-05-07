package fr.vexia.api;

import fr.vexia.api.data.connectors.DatabaseConnection;
import fr.vexia.api.data.connectors.DatabaseConnectionBuilder;
import fr.vexia.api.data.redis.RedisConnection;

public class VexiaAPI {

    public void init() {
        DatabaseConnectionBuilder.aDatabaseConnection().withHost("127.0.0.1")
                .withUser("minecraft").withPassword("FcrXeZPZtksLeu3S2Y3GcJU5wSFTSw8K").withPort(5432).withDatabase("minecraft")
                .build();

        new RedisConnection("127.0.0.1", null, 6379);
    }

    public void stop() {
        RedisConnection.close();
        DatabaseConnection.close();
    }

}
