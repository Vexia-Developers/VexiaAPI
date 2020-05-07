package fr.vexia.proxy.listeners;

import fr.vexia.api.data.manager.FriendManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.options.Option;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProxyQuitListener implements Listener {

    private VexiaProxy proxy;

    public ProxyQuitListener(VexiaProxy proxy) {
        this.proxy = proxy;
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        proxy.getStaffManager().removeStaff(player.getUniqueId());
        UUID uuid = player.getUniqueId();
        VexiaPlayer vexiaPlayer = PlayerManager.get(uuid);
        if (vexiaPlayer == null) return;

        vexiaPlayer.setLastJoin(new Date());
        vexiaPlayer.setServer(null);

        List<VexiaPlayer> friends = FriendManager.getFriends(player.getUniqueId());
        for (VexiaPlayer friend : friends) {
            if (friend.getOption(Option.FRIEND_NOTIFICATION) == Option.OptionValue.OFF) {
                continue;
            }

            ProxiedPlayer playerFriend = ProxyServer.getInstance().getPlayer(friend.getUUID());
            if (playerFriend == null) {
                continue;
            }

            playerFriend.sendMessage(new TextBuilder("§e" + player.getName() + " §6vient de se déconnecter").build());
        }

        PlayerManager.save(vexiaPlayer);
    }


}
