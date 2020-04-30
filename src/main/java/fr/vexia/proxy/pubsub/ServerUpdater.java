package fr.vexia.proxy.pubsub;

import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.data.redis.pubsub.IPatternReceiver;
import fr.vexia.api.servers.VexiaServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class ServerUpdater implements IPatternReceiver {

    @Override
    public void receive(String pattern, String channel, String message) {

        if (channel.startsWith("serverupdater-remove") && !message.startsWith("hub")) {
            ProxyServer.getInstance().getServers().remove(message);
            return;
        }

        if (channel.startsWith("serverupdater-create") && !ProxyServer.getInstance().getServers().containsKey(message)) {
            VexiaServer server = ServerManager.get(message);
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", server.getPort());
            ServerInfo info = ProxyServer.getInstance().constructServerInfo(server.getName(), address, "", false);
            ProxyServer.getInstance().getServers().put(message, info);
        }

    }
}
