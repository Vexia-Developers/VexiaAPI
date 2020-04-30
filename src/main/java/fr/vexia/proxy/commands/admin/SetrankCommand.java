package fr.vexia.proxy.commands.admin;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import fr.vexia.proxy.utils.TimeFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.Date;

public class SetrankCommand extends VexiaCommand {

    public SetrankCommand(VexiaProxy proxy) {
        super(proxy, "setrank");
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
        if (args.length < 3) {
            help(sender);
            return;
        }

        VexiaPlayer player = PlayerManager.get(args[0]);
        if (player == null) {
            sender.sendMessage(new TextBuilder("Erreur: Ce joueur n'existe pas !", ChatColor.RED).build());
            return;
        }

        Rank rank = Rank.getRankByName(args[1]);
        if (rank == null) {
            sender.sendMessage(new TextBuilder("Erreur: Ce grade n'existe pas !", ChatColor.RED).build());
            return;
        }

        Date end = null;
        String time = args[2];

        if (!time.equalsIgnoreCase("perm")) {
            long duration = TimeFormat.parsePeriod(time);
            if (duration == -1) {
                sender.sendMessage(new TextBuilder("Erreur: Durée invalide !", ChatColor.RED).build());
                return;
            }

            end = new Date(System.currentTimeMillis() + duration);
        }

        player.setRank(rank);
        player.setRankExpires(end);
        PlayerManager.save(player);

        sender.sendMessage(new TextBuilder(
                String.format("§eLe rôle de §7%s §ea été changé sur %s §ejusqu'à §7%s§e.",
                        player.getName(), rank.getColoredPrefix(),
                        (end == null ? "Permanant" : TimeFormat.formatDate(end.getTime()))))
                .build());
    }

    @Override
    protected void help(CommandSender sender) {
        sender.sendMessage(new TextBuilder("Syntaxe: /setrank <joueur> <grade> <durée | perm>", ChatColor.RED).build());
    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
