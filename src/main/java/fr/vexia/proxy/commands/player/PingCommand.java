package fr.vexia.proxy.commands.player;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PingCommand extends VexiaCommand {

    public PingCommand(VexiaProxy proxy) {
        super(proxy, "ping", null, "lag", "ms");
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

        sender.sendMessage(new TextBuilder("§6§m                        [§e§l PING §6§m]                        ").build());
        sender.sendMessage(new TextBuilder("§6Serveur: §e"+player.getServer().getInfo().getName()).build());
        sender.sendMessage(new TextBuilder("§6Vous avez actuellement §e"+player.getPing()+" ms").build());
    }

    @Override
    protected void help(CommandSender sender) {
    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
