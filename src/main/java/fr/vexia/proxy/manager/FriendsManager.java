package fr.vexia.proxy.manager;

import fr.vexia.api.data.manager.FriendManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.options.Option;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FriendsManager {

    private HashMap<UUID, UUID> invitations;

    public FriendsManager() {
        this.invitations = new HashMap<>();
    }

    public void add(ProxiedPlayer player, String target) {
        if (player.getName().equalsIgnoreCase(target)) {
            player.sendMessage(new TextBuilder("Vous ne pouvez pas vous demander en ami.", ChatColor.RED).build());
            return;
        }

        VexiaPlayer targetAccount = PlayerManager.get(target);
        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(target);
        if (targetAccount == null || targetPlayer == null) {
            player.sendMessage(new TextBuilder("Vous ne pouvez pas demander en ami un joueur qui n'est pas connecté", ChatColor.RED).build());
            return;
        }

        if (FriendManager.areFriends(player.getUniqueId(), targetPlayer.getUniqueId())) {
            player.sendMessage(new TextBuilder("Vous êtes déjà ami avec ce joueur", ChatColor.RED).build());
            return;
        }

        if (invitations.containsKey(player.getUniqueId()) &&
                invitations.get(player.getUniqueId()).equals(targetPlayer.getUniqueId())) {
            player.sendMessage(new TextBuilder("Vous avez déjà envoyé une demande d'amis à ce joueur", ChatColor.RED).build());
            return;
        }

        if (targetAccount.getOption(Option.FRIEND_REQUEST) == Option.OptionValue.OFF) {
            player.sendMessage(new TextBuilder("Le joueur a désactivé les demandes d'amis", ChatColor.RED).build());
            return;
        }

        if (invitations.containsKey(targetPlayer.getUniqueId()) &&
                invitations.get(targetPlayer.getUniqueId()).equals(player.getUniqueId())) {
            accept(targetPlayer, player);
            invitations.remove(targetPlayer.getUniqueId());
            return;
        }

        player.sendMessage(new TextBuilder("§6Invitation envoyée a §e" + targetPlayer.getName()).build());
        sendFormatInvitation(player, targetPlayer);
        invitations.put(player.getUniqueId(), targetPlayer.getUniqueId());
    }

    public void remove(ProxiedPlayer player, String target) {
        VexiaPlayer targetAccount = PlayerManager.get(target);
        if (targetAccount == null || !FriendManager.areFriends(player.getUniqueId(), targetAccount.getUUID())) {
            player.sendMessage(new TextBuilder("Vous n'êtes pas ami avec ce joueur", ChatColor.RED).build());
            return;
        }

        FriendManager.delete(player.getUniqueId(), targetAccount.getUUID());
        player.sendMessage(new TextBuilder("§6Vous avez retiré §e" + targetAccount.getName() + " §6de vos amis").build());

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetAccount.getUUID());
        if (targetPlayer != null) {
            targetPlayer.sendMessage(new TextBuilder("§e" + player.getName() + " §6vous a retiré de ses amis").build());
        }
    }

    public void tp(ProxiedPlayer player, String target) {
        VexiaPlayer targetAccount = PlayerManager.get(target);
        if (targetAccount == null || !FriendManager.areFriends(player.getUniqueId(), targetAccount.getUUID())) {
            player.sendMessage(new TextBuilder("Vous n'êtes pas ami avec ce joueur", ChatColor.RED).build());
            return;
        }

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetAccount.getUUID());
        if (targetPlayer == null) {
            player.sendMessage(new TextBuilder("Ce joueur n'est pas connecté", ChatColor.RED).build());
            return;
        }

        ServerInfo targetServer = targetPlayer.getServer().getInfo();
        if (targetServer.equals(player.getServer().getInfo())) {
            player.sendMessage(new TextBuilder("Vous êtes déjà connecté à ce serveur", ChatColor.RED).build());
            return;
        }

        player.sendMessage(new TextBuilder("§6Téléportation sur le serveur de §e" + targetPlayer.getName()).build());
        player.connect(targetPlayer.getServer().getInfo());
    }

    public void accept(ProxiedPlayer player, String target) {
        VexiaPlayer targetAccount = PlayerManager.get(target);
        if (targetAccount == null) {
            player.sendMessage(new TextBuilder("Ce joueur n'existe pas", ChatColor.RED).build());
            return;
        }

        if(!invitations.containsKey(targetAccount.getUUID()) ||
                !invitations.get(targetAccount.getUUID()).equals(player.getUniqueId())) {
            player.sendMessage(new TextBuilder("Ce joueur ne vous a pas demandé en ami", ChatColor.RED).build());
            return;
        }

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetAccount.getUUID());
        if(targetPlayer == null) {
            player.sendMessage(new TextBuilder("Ce joueur n'est pas connecté", ChatColor.RED).build());
            return;
        }

        accept(targetPlayer, player);
        invitations.remove(targetPlayer.getUniqueId());
    }

    public void list(ProxiedPlayer player) {
        List<VexiaPlayer> friends = FriendManager.getFriends(player.getUniqueId());
        List<TextComponent> send = new ArrayList<>();
        send.add(new TextBuilder("§6§m                        [§e§l AMIS §6§m]                        ").build());

        for (VexiaPlayer friend : friends) {
            String status = friend.getServer() == null ? "§7§c(Offline§7)" : "§7(§aConnecté§7)";
            send.add(new TextBuilder("§8- §e" + friend.getName() + " " + status).build());
        }

        if (send.size() == 1) {
            send.add(new TextBuilder("§7Vous n'avez pas encore d'ami").build());
            send.add(new TextBuilder("§7Utilise §6/f add <pseudo>§7 pour ajouter un ami").build());
        }

        send.forEach(player::sendMessage);
    }

    private void accept(ProxiedPlayer player, ProxiedPlayer targetPlayer) {
        targetPlayer.sendMessage(new TextBuilder("§6Vous êtes désormais ami avec §e"+player.getName()).build());
        player.sendMessage(new TextBuilder("§6Vous êtes désormais ami avec §e"+targetPlayer.getName()).build());
        FriendManager.create(player.getUniqueId(), targetPlayer.getUniqueId());
    }

    private void sendFormatInvitation(ProxiedPlayer sender, ProxiedPlayer target) {
        target.sendMessage(new TextBuilder("§6Vous avez reçu une requête d'ami de §e" + sender.getName()).build());
        TextComponent text = new TextBuilder("§a➥ /friend accept " + sender.getName()).build();
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Accepter la requête d'ami").create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + sender.getName()));
        target.sendMessage(text);
    }
}