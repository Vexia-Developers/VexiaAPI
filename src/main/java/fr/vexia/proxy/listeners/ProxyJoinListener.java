package fr.vexia.proxy.listeners;

import fr.vexia.api.data.manager.FriendManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.manager.SanctionManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.options.Option;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.api.sanctions.SanctionType;
import fr.vexia.api.sanctions.VexiaSanction;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.utils.TextBuilder;
import fr.vexia.proxy.utils.TimeFormat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProxyJoinListener implements Listener {

    private VexiaProxy proxy;

    public ProxyJoinListener(VexiaProxy proxy) {
        this.proxy = proxy;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        PendingConnection connection = event.getConnection();
        UUID uuid = connection.getUniqueId();
        String ip = connection.getAddress().getAddress().getHostAddress();
        String pseudo = connection.getName();

        VexiaPlayer vexiaPlayer = PlayerManager.get(uuid);
        if (vexiaPlayer != null) {
            if (hasSanction(uuid, event)) {
                return;
            }

            Date currentTime = new Date();
            vexiaPlayer.setName(pseudo);
            vexiaPlayer.setIP(ip);
            vexiaPlayer.setLastJoin(currentTime);

            if (vexiaPlayer.getRankExpires() != null && currentTime.after(vexiaPlayer.getRankExpires())) {
                vexiaPlayer.setRank(Rank.JOUEUR);
                vexiaPlayer.setRankExpires(null);
            }

            if (vexiaPlayer.getRank().getId() >= Rank.MODERATEUR.getId()) {
                proxy.getStaffManager().addStaff(vexiaPlayer.getUUID());
            }

            for (VexiaPlayer playerIP : PlayerManager.getWithIP(vexiaPlayer.getIP())) {
                if (playerIP.getUUID().equals(vexiaPlayer.getUUID())) continue;
                boolean ban = SanctionManager.getActiveSanction(playerIP.getUUID(), SanctionType.BAN) != null;
                if (!ban) continue;


                proxy.getStaffManager().broadcast(new TextBuilder("§5§l[§fVexiaSanction§5§l] §d" + connection.getName() +
                        " §cviens de se connecter avec un compte bannis sur son IP"));
                break;
            }


            List<VexiaPlayer> friends = FriendManager.getFriends(uuid);
            for (VexiaPlayer friend : friends) {
                if (friend.getOption(Option.FRIEND_NOTIFICATION) == Option.OptionValue.OFF) {
                    continue;
                }

                ProxiedPlayer playerFriend = ProxyServer.getInstance().getPlayer(friend.getUUID());
                if (playerFriend == null) {
                    continue;
                }

                playerFriend.sendMessage(new TextBuilder("§e" + connection.getName() + " §6vient de se connecter").build());
            }

        } else {
            vexiaPlayer = new VexiaPlayer(uuid, pseudo, ip, null);
        }

        PlayerManager.save(vexiaPlayer);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        player.setTabHeader(new TextComponent("§r\n§6§lVEXIA§f§l NETWORK §7\n"),
                new TextComponent("§r\n  §6Forum et Boutique sur §bhttps://vexia.fr  §r\n"));
        Title title = ProxyServer.getInstance().createTitle();
        title.fadeIn(10).fadeOut(20).stay(60);
        title.title(new TextComponent("§6§lVEXIA"));
        title.subTitle(new TextComponent("§eBienvenue " + player.getName()));
        title.send(player);
    }

    private boolean hasSanction(UUID uuid, LoginEvent event) {
        VexiaSanction sanction = SanctionManager.getActiveSanction(uuid, SanctionType.BAN);
        if (sanction == null) {
            return false;
        }
        VexiaPlayer moderator = PlayerManager.get(sanction.getModerator());

        String sanctionText = String.format("§5§l§m-----§f VexiaSanction §5§l§m-----\n\n" +
                        "§dPar: §7%s \n" +
                        "§dType: §7%s \n" +
                        "§dLe: §7%s \n" +
                        "§dFin le: §7%s \n" +
                        "§dRaison: §7%s\n\n" +
                        "§5§l§m---------------------------",
                moderator.getName(), sanction.getSanctionType().getName(),
                TimeFormat.formatDate(sanction.getStart().getTime()),
                (sanction.getEnd() == null ? "Jamais" : TimeFormat.formatDate(sanction.getEnd().getTime())),
                sanction.getReason());

        event.setCancelled(true);
        event.setCancelReason(sanctionText);
        return true;
    }

    @EventHandler
    public void onServerConnect(ServerConnectedEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        Server server = event.getServer();
        VexiaPlayer vexiaPlayer = PlayerManager.get(proxiedPlayer.getUniqueId());
        vexiaPlayer.setServer(server.getInfo().getName());
        PlayerManager.save(vexiaPlayer);
    }

}
