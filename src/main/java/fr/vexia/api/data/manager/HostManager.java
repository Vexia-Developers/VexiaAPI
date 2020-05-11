package fr.vexia.api.data.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.vexia.api.data.executors.DatabaseExecutor;
import fr.vexia.api.data.redis.RedisConnection;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.servers.hosts.HostGameType;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import redis.clients.jedis.Jedis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class HostManager  {

    /*
    CREATE TABLE hosts
    (
        "ownerUUID" uuid NOT NULL,
        id serial NOT NULL,
        "borderSpeed" real NOT NULL DEFAULT 0.5,
        "maxPlayer" integer NOT NULL DEFAULT 20,
        teams integer NOT NULL DEFAULT 8,
        "borderSize" integer NOT NULL DEFAULT 1000,
        "borderReduce" integer NOT NULL DEFAULT 20,
        "timeBeforePVP" integer NOT NULL DEFAULT 20,
        type character varying(255) NOT NULL DEFAULT 'UHC',
        neather boolean NOT NULL DEFAULT true,
        PRIMARY KEY (id)
    );
     */


    private static final String TABLE_NAME = "hosts";

    private static final String SAVE = "INSERT INTO "+TABLE_NAME+"(id, ownerUUID, type, maxPlayer, teams, borderSize, borderSpeed, borderReduce, timeBeforePVP, neather) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET "
            + "id = EXCLUDED.id, ownerUUID = EXCLUDED.ownerUUID, type = EXCLUDED.type, "

            + "maxPlayer = EXCLUDED.maxPlayer, " +
              "teams = EXCLUDED.teams, " +
              "borderSize = EXCLUDED.borderSize, " +
              "borderSpeed = EXCLUDED.borderSpeed, " +
              "borderReduce = EXCLUDED.borderReduce, " +
              "timeBeforePVP = EXCLUDED.timeBeforePVP, " +
              "neather = EXCLUDED.neather";

    private static final String GET_BY_UUID = "SELECT * FROM "+TABLE_NAME+" WHERE ownerUUID = ?";
    private static final String GET_BY_ID = "SELECT * FROM "+TABLE_NAME+" WHERE id = ?";

    private static final String DELETE_BY_ID = "DELETE FROM "+TABLE_NAME+" WHERE id = ?";


    public static void create(VexiaHostConfig hostConfig){
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(SAVE);
            statement.setObject(1, hostConfig.getId());
            statement.setObject(2, hostConfig.getOwnerUUID());
            statement.setObject(3, hostConfig.getType().toString());
            statement.setObject(4, hostConfig.getMaxPlayer());
            statement.setObject(5, hostConfig.getTeams());
            statement.setObject(6, hostConfig.getBorderSize());
            statement.setObject(7, hostConfig.getBorderSpeed());
            statement.setObject(8, hostConfig.getBorderReduce());
            statement.setObject(9, hostConfig.getTimeBeforePVP());
            statement.setObject(10, hostConfig.isNeather());
            statement.executeUpdate();
        });
    }

    public static void delete(int id) {
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(DELETE_BY_ID);
            statement.setObject(1, id);
            statement.executeUpdate();
        });
    }

    public static List<VexiaHostConfig> getConfigsByOwner(UUID ownerUUID) {
        List<VexiaHostConfig> hostConfigs = new ArrayList<>();
        DatabaseExecutor.executeVoidQuery(data -> {
            PreparedStatement statement = data.prepareStatement(GET_BY_UUID);
            statement.setObject(1, ownerUUID);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                hostConfigs.add(new VexiaHostConfig(rs.getInt("id"), UUID.fromString(rs.getString("ownerUUID")),
                        HostGameType.valueOf(rs.getString("type")), null, rs.getInt("maxPlayer"), rs.getInt("teams"),
                        rs.getInt("borderSize"), rs.getFloat("borderSpeed"), rs.getInt("borderReduce"), rs.getInt("timeBeforePvp"),
                        rs.getBoolean("neather")));
            }
        });
        return hostConfigs;
    }


    public static List<VexiaHostConfig> getRedisHosts(){
        try (Jedis jedis = RedisConnection.getJedis()) {
            Set<String> hostKeys = jedis.keys("host:*");
            return hostKeys.stream().map((key) -> getRedisHost(key.replaceFirst("host:", ""))).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VexiaHostConfig getRedisHost(String serverName){
        try (Jedis jedis = RedisConnection.getJedis()) {
            Gson gson = new GsonBuilder().create();
            String JSONvalue = jedis.get("host:"+serverName);
            return gson.fromJson(JSONvalue, VexiaHostConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VexiaHostConfig getRedisHostByOwner(UUID ownerUUID){
        return getRedisHosts().stream().filter((server) -> server.getOwnerUUID().equals(ownerUUID)).findFirst().orElse(null);
    }

    public static void saveRedisHost(VexiaHostConfig hostConfig){
        try (Jedis jedis = RedisConnection.getJedis()) {
            Gson gson = new GsonBuilder().create();
            String JSONvalue = gson.toJson(hostConfig);
            jedis.set("host:" + hostConfig.getServerName(), JSONvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteRedisHost(VexiaHostConfig serverName){
        try (Jedis jedis = RedisConnection.getJedis()) {
            jedis.del("host:" + serverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pubCreateServer(VexiaHostConfig hostConfig){
        Gson gson = new GsonBuilder().create();
        String JSONvalue = gson.toJson(hostConfig);
        PubSubAPI.publish("create_host", JSONvalue);
    }

}
