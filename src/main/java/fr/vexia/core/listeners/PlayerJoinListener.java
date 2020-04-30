package fr.vexia.core.listeners;

import fr.vexia.core.manager.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final TeleportManager teleportManager;

    public PlayerJoinListener(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if(teleportManager.hasPending(playerName)) {
            String nameTo = teleportManager.getPending(playerName);
            teleportManager.removePending(playerName);
            Player playerTo = Bukkit.getPlayer(nameTo);
            if(playerTo == null) {
                player.sendMessage("§cErreur: Impossible de se téléporter à ce joueur.");
                return;
            }

            player.teleport(playerTo);
            player.sendMessage("§aTéléportation réussie");
        }
    }

}
