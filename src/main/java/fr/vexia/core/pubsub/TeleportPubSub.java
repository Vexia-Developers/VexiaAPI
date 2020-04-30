package fr.vexia.core.pubsub;

import fr.vexia.core.manager.TeleportManager;
import fr.vexia.api.data.redis.pubsub.IPatternReceiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TeleportPubSub implements IPatternReceiver {

    private final TeleportManager teleportManager;

    public TeleportPubSub(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @Override
    public void receive(String pattern, String channel, String message) {
        JSONParser jsonParser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) jsonParser.parse(message);
        } catch (ParseException e) { return; }

        String from = json.get("from").toString();
        String to = json.get("to").toString();

        Player playerFrom = Bukkit.getPlayer(from);
        if(playerFrom == null) {
            teleportManager.addPending(from, to);
            return;
        }

        Player playerTo = Bukkit.getPlayer(to);
        if(playerTo == null) {
            return;
        }

        playerFrom.teleport(playerTo);

    }
}
