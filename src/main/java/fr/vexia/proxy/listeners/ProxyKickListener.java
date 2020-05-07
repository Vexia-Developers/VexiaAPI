package fr.vexia.proxy.listeners;

import fr.vexia.api.data.manager.ServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyKickListener implements Listener {

    @EventHandler
    public void onKick(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(ServerManager.get("hub01") != null) {
            ServerInfo hub01 = ProxyServer.getInstance().getServerInfo("hub01");
            player.connect(hub01);
        } else {
            player.disconnect(event.getKickReasonComponent());
        }
    }
}
