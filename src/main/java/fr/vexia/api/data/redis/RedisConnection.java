package fr.vexia.api.data.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

    private static JedisPool pool;

    public RedisConnection(String host, String password, int port) {
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Jedis.class.getClassLoader());
        pool = new JedisPool(new JedisPoolConfig(), host, port, 2000, password);
        Thread.currentThread().setContextClassLoader(previous);
    }

    public static void close() {
        pool.destroy();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public JedisPool getPool() {
        return pool;
    }
}
