package fr.vexia.proxy.commands.moderation;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.manager.SanctionManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.api.sanctions.VexiaSanction;
import fr.vexia.api.sanctions.SanctionType;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TabComplete;
import fr.vexia.proxy.utils.TextBuilder;
import fr.vexia.proxy.utils.TimeFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoCommand extends VexiaCommand {

    public PlayerInfoCommand(VexiaProxy vexiaProxy) {
        super(vexiaProxy, "pinfo", null, "uinfo", "playerinfo", "userinfo");
    }

    @Override
    public boolean onlyPlayer() {
        return false;
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

        VexiaPlayer player;

        if (args[0].contains(".")) {
            List<VexiaPlayer> players = PlayerManager.getWithIP(args[0]);
            if (players.size() == 0) {
                sender.sendMessage(new TextBuilder("Erreur: Aucun joueur trouvé sur cette IP !", ChatColor.RED).build());
                return;
            }
            player = players.get(0);
        } else {
            player = PlayerManager.get(args[0]);
            if (player == null) {
                sender.sendMessage(new TextBuilder("Erreur: Ce joueur n'existe pas !", ChatColor.RED).build());
                return;
            }
        }

        ArrayList<TextComponent> send = new ArrayList<>();
        send.add(new TextBuilder("§6§m               [§e§l " + player.getName() + " §6§m]               ").build());

        send.add(new TextBuilder("Première connection: ", ChatColor.GOLD)
                .addTexte(TimeFormat.formatDate(player.getFirstJoin().getTime()), ChatColor.WHITE)
                .build());

        send.add(new TextBuilder("§6Grade: §f" + player.getRank().getColor() + player.getRank().getName() +
                " §7(Termine le: " + (player.getRankExpires() == null ? "Jamais" : TimeFormat.formatDate(player.getRankExpires().getTime())) + ")").build());

        if (account == null || account.getRank().getId() >= Rank.ADMINISTRATEUR.getId()) {
            send.add(new TextBuilder("§6Coins: §f" + player.getCoins()).build());
            send.add(new TextBuilder("§6Crédits: §f" + player.getCredits()).build());
        }

        VexiaSanction ban = SanctionManager.getActiveSanction(player.getUUID(), SanctionType.BAN);
        send.add(printSanction(ban, SanctionType.BAN));

        VexiaSanction mute = SanctionManager.getActiveSanction(player.getUUID(), SanctionType.MUTE);
        send.add(printSanction(mute, SanctionType.MUTE));

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player.getUUID());
        if (target != null) {
            TextComponent baseConnected = new TextBuilder("§6Connecté: §aOui").build();
            TextComponent descConnected = new TextBuilder(" §7(Serveur: " + target.getServer().getInfo().getName() + ", " + target.getPing() + "ms)").build();
            descConnected.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Rejoindre le serveur").color(ChatColor.GOLD).create()));
            descConnected.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/servers " + target.getServer().getInfo().getName()));
            baseConnected.addExtra(descConnected);
            send.add(baseConnected);
        } else {
            send.add(new TextBuilder("§6Connecté: §cNon §7(Dernière connection: " + TimeFormat.formatDate(player.getLastJoin().getTime()) + ")").build());
        }

        TextComponent builder = null;
        for (VexiaPlayer targets : PlayerManager.getWithIP(player.getIP())) {
            ProxiedPlayer targetsPlayer = ProxyServer.getInstance().getPlayer(targets.getUUID());

            ChatColor color;
            if (targetsPlayer != null)
                color = ChatColor.GREEN;
            else {
                boolean isBan = SanctionManager.getActiveSanction(targets.getUUID(), SanctionType.BAN) != null;
                if (isBan) color = ChatColor.RED;
                else color = ChatColor.GRAY;
            }

            TextComponent accounts = new TextBuilder(color + targets.getName() + " ").build();
            accounts.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Voir le profil").color(ChatColor.GOLD).create()));
            accounts.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pinfo " + targets.getName()));

            if (builder == null) {
                builder = accounts;
            } else {
                builder.addExtra(accounts);
            }

        }


        TextComponent component = new TextBuilder("§6Compte(s) : §f").build();
        if (builder != null)
            component.addExtra(builder);
        send.add(component);

        String IP = player.getIP();

        if (account == null || account.getRank().getId() >= Rank.ADMINISTRATEUR.getId()) {
            TextComponent ip = new TextBuilder("§6IP: §f" + IP).build();
            ip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Plus d'info sur l'ip").color(ChatColor.GRAY).create()));
            ip.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://ip-api.com/#" + IP));
            send.add(ip);
        }

        send.forEach(sender::sendMessage);
    }

    @Override
    public void help(CommandSender sender) {
        sender.sendMessage(new TextBuilder("Syntaxe : /pinfo <name>", ChatColor.RED).build());
    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (ProxiedPlayer players : ((ProxiedPlayer) sender).getServer().getInfo().getPlayers()) {
                list.add(players.getName());
            }
            int i = args.length - 1;
            return new TabComplete(args[i], list).getReponse();
        }
        return new ArrayList<>();
    }

    private TextComponent printSanction(VexiaSanction sanction, SanctionType sanctionType) {
        TextBuilder text = new TextBuilder("§6" + sanctionType.getName() + ": ");
        if (sanction != null) {
            VexiaPlayer moderator = PlayerManager.get(sanction.getModerator());

            String moderatorUsername = moderator != null ? moderator.getName() : "Inconnu";
            String end = (sanction.getEnd() == null ? "Jamais" : TimeFormat.formatDate(sanction.getEnd().getTime()));
            String start = TimeFormat.formatDate(sanction.getStart().getTime());

            text.addTexte(String.format("§aOui §f(§6Par: §e%s, §6Le: §e%s, §6Fin le: §e%s, §6Raison: §e%s§f)",
                    moderatorUsername, start, end, sanction.getReason()));
        } else
            text.addTexte("§cNon");
        return text.build();
    }


}
