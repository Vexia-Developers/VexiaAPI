package fr.vexia.proxy.commands.player;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.manager.FriendsManager;
import fr.vexia.proxy.utils.TabComplete;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsCommand extends VexiaCommand {

    private FriendsManager manager;

    public FriendsCommand(VexiaProxy proxy) {
        super(proxy, "friend", null, "friends", "f");
        this.manager = proxy.getFriendsManager();
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

        if (args.length == 0 || (args.length == 1 && !args[0].equalsIgnoreCase("list"))) {
            help(player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                manager.add(player, args[1]);
                break;
            case "remove":
                manager.remove(player, args[1]);
                break;
            case "tp":
                manager.tp(player, args[1]);
                break;
            case "accept":
                manager.accept(player, args[1]);
                break;
            case "list":
                manager.list(player);
                break;
            default:
                help(player);
                break;
        }
    }

    @Override
    protected void help(CommandSender sender) {
        sender.sendMessage(new TextBuilder("§6§m                        [§e§l AMIS §6§m]                        ").build());
        sender.sendMessage(new TextBuilder("§e/friends add <pseudo> : §fAjouter un joueur en ami").build());
        sender.sendMessage(new TextBuilder("§e/friends remove <pseudo> : §Retirer un joueur en ami").build());
        sender.sendMessage(new TextBuilder("§e/friends tp <pseudo> : §fSe téléporter au serveur du joueur").build());
        sender.sendMessage(new TextBuilder("§e/friends accept <pseudo> : §fAccepter l'invitation du joueur").build());
        sender.sendMessage(new TextBuilder("§e/friends list : §fVoir la liste des joueurs en ami").build());
    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        if (args.length == 1) {
            return new TabComplete(args[0], Arrays.asList("add", "remove", "tp", "accept", "list", "on", "off")).getReponse();
        } else if (args.length == 2) {
            if ("add".equals(args[0].toLowerCase())) {
                List<String> list = new ArrayList<>();
                for (ProxiedPlayer players : ((ProxiedPlayer) sender).getServer().getInfo().getPlayers()) {
                    list.add(players.getName());
                }
                return new TabComplete(args[1], list).getReponse();
            }
        }
        return new ArrayList<>();

    }

}
