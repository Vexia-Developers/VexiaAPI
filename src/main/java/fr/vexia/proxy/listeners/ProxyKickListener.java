package fr.vexia.proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyKickListener implements Listener {

    @EventHandler
    public void onKick(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        player.connect(ProxyServer.getInstance().getServerInfo("hub01"));
        player.sendMessage(event.getKickReasonComponent());
    }
}
