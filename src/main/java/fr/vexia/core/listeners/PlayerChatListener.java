package fr.vexia.core.listeners;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        VexiaPlayer vexiaPlayer = PlayerManager.get(player.getUniqueId());
        if(vexiaPlayer == null) return;

        if(vexiaPlayer.getRank() == Rank.JOUEUR) {
            event.setFormat(vexiaPlayer.getRank().getColor()+"%1$s §f: %2$s");
            return;
        }

        event.setFormat(vexiaPlayer.getRank().getColoredPrefix()+" %1$s §f: %2$s");
    }

}
