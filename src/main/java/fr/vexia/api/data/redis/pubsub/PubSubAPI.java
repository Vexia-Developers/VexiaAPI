package fr.vexia.api.data.redis.pubsub;

import fr.vexia.api.data.redis.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PubSubAPI {

    public static void publish(String channel, String message) {
        try (Jedis jedis = RedisConnection.getJedis()) {
            jedis.publish(channel, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void subscribe(String channel, IPacketsReceiver receiver) {
        Thread thread = new Thread(() -> {
            try (Jedis jedis = RedisConnection.getJedis()) {
                jedis.subscribe(new JedisPubSub() {

                    @Override
                    public void onMessage(String channel, String message) {
                        receiver.receive(channel, message);
                    }

                }, channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "sub (c = " + channel + ")");
        thread.start();
    }

    public static void psubscribe(String pattern, IPatternReceiver receiver) {
        Thread thread = new Thread(() -> {
            try (Jedis jedis = RedisConnection.getJedis()) {
                jedis.psubscribe(new JedisPubSub() {

                    @Override
                    public void onPMessage(String pattern, String channel, String message) {
                        receiver.receive(pattern, channel, message);
                    }

                }, pattern);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "sub (p = " + pattern + ")");
        thread.start();
    }
}
