package fr.vexia.api.data.redis.pubsub;

public interface IPacketsReceiver {
    void receive(String channel, String message);
}
