package fr.vexia.proxy.commands.player;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubCommand extends VexiaCommand {

    public HubCommand(VexiaProxy proxy) {
        super(proxy, "hub", null, "lobby", "spawn");
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public Rank minimumGrade() {
        return Rank.JOUEUR;
    }

    @Override
    public void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        String serverName = player.getServer().getInfo().getName();
        if(serverName.startsWith("hub")) {
            player.sendMessage(new TextBuilder("Vous êtes déjà connecté au lobby.", ChatColor.RED).build());
            return;
        }

        player.connect(ProxyServer.getInstance().getServerInfo("hub01"));
    }

    @Override
    protected void help(CommandSender sender) {

    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }

}
