package fr.vexia.proxy.commands.moderation;

import com.google.gson.JsonObject;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TpCommand extends VexiaCommand {

    public TpCommand(VexiaProxy proxy) {
        super(proxy, "tp");
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public Rank minimumGrade() {
        return Rank.MODERATEUR;
    }

    @Override
    public void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        if (args.length == 0) {
            help(sender);
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(new TextBuilder("§cErreur: Ce joueur n'est pas connecté !").build());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo serverInfo = target.getServer().getInfo();
        if (player.getServer().getInfo() != serverInfo) {
            player.connect(serverInfo);
        }

        sender.sendMessage(new TextBuilder("§eTéléportation vers " + target.getName() + "...").build());

        JsonObject data = new JsonObject();
        data.addProperty("from", account.getName());
        data.addProperty("to", target.getName());
        PubSubAPI.publish("teleport", data.toString());
    }

    @Override
    protected void help(CommandSender sender) {

    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
