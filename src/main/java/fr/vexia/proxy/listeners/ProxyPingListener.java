package fr.vexia.proxy.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {
    private Configuration configuration;

    public ProxyPingListener(Configuration configuration) {
        this.configuration = configuration;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing ping = event.getResponse();

        ServerPing.Protocol protocol = ping.getVersion();

        if (configuration.getBoolean("maintenance")) {
            protocol = new ServerPing.Protocol(ChatColor.RED + "En Maintenance...", 0);
        } else if (configuration.getInt("max") <= ProxyServer.getInstance().getOnlineCount()) {
            protocol = new ServerPing.Protocol("§4Plein §8- §7" + ping.getPlayers().getOnline() + "§8/§7" + configuration.getInt("max"), 0);
        }

        ping.setVersion(protocol);
        ping.setPlayers(new ServerPing.Players(configuration.getInt("max"), ping.getPlayers().getOnline(), ping.getPlayers().getSample()));

        String ligne1 = ChatColor.translateAlternateColorCodes('&', configuration.getString("motd.ligne1"));
        String ligne2 = ChatColor.translateAlternateColorCodes('&', configuration.getString("motd.ligne2"));
        ping.setDescription(ligne1 + "\n" + ChatColor.RESET + ligne2);
        event.setResponse(ping);
    }


}
