package fr.vexia.proxy.commands.admin;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.PrefixUtils;
import fr.vexia.proxy.utils.TabComplete;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;
import java.util.List;

public class BroadcastCommand extends VexiaCommand {

    public BroadcastCommand(VexiaProxy vexiaProxy) {
        super(vexiaProxy, "broadcast", null, "bc", "say");
    }

    @Override
    public boolean onlyPlayer() {
        return false;
    }

    @Override
    public Rank minimumGrade() {
        return Rank.ADMINISTRATEUR;
    }

    @Override
    public void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        if (args.length >= 1) {
            String message = ChatColor.translateAlternateColorCodes('&', Strings.join(args, " ")).replace("\\n", "\n");
            if (message.startsWith("&-")) {
                ProxyServer.getInstance().broadcast(new TextBuilder("§6§m                                                       ").build());
                ProxyServer.getInstance().broadcast(new TextBuilder(message.replaceFirst("&-", "")).build());
                ProxyServer.getInstance().broadcast(new TextBuilder("§6§m                                                       ").build());
            } else if (message.startsWith("&h") || account == null) {
                TextComponent broadcast = new TextBuilder(PrefixUtils.BROADCAST + message.replaceFirst("&h", "")).build();
                ProxyServer.getInstance().broadcast(broadcast);
            } else {
                TextComponent broadcast = new TextBuilder(PrefixUtils.BROADCAST).addTexte(account.getRank().getColoredPrefix() + " " + sender.getName() + "§8 : ").addTexte(message).build();
                ProxyServer.getInstance().broadcast(broadcast);
            }
        } else {
            help(sender);
        }
    }

    @Override
    public void help(CommandSender sender) {
        sender.sendMessage(new TextBuilder("Syntaxe : /broadcast <message>", ChatColor.RED).build());
    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            List<String> list = new ArrayList<>();
            for (ProxiedPlayer players : ((ProxiedPlayer) sender).getServer().getInfo().getPlayers()) {
                list.add(players.getName());
            }
            int i = args.length - 1;
            return new TabComplete(args[i], list).getReponse();
        }
        return new ArrayList<>();
    }


}
