package fr.vexia.api.data.redis.pubsub;

public interface IPatternReceiver {
    void receive(String pattern, String channel, String message);
}
