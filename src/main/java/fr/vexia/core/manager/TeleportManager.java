package fr.vexia.core.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.vexia.api.servers.VexiaServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.HashMap;

public class TeleportManager {

    private HashMap<String, String> pending;

    public TeleportManager() {
        this.pending = new HashMap<>();
    }

    public void addPending(String from, String to) {
        pending.put(from, to);
    }

    public boolean hasPending(String from) {
        return pending.containsKey(from);
    }

    public String getPending(String from) {
        return pending.get(from);
    }

    public void removePending(String from) {
        pending.remove(from);
    }

    public static void sendServer(Plugin plugin, Player player, VexiaServer vexiaServer) {
        Messenger messenger = Bukkit.getMessenger();
        if(!messenger.isOutgoingChannelRegistered(plugin, "BungeeCord")) {
            messenger.registerOutgoingPluginChannel(plugin, "BungeeCord");
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(vexiaServer.getName());
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

}
