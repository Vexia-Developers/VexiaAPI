package fr.vexia.core.staff.freeze;

import fr.vexia.core.staff.StaffManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isFreeze(player)) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        StaffManager.get().freezeLogout(player);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isFreeze(player)) {
            event.setCancelled(true);
            player.sendMessage("§cVous ne pouvez pas executer de commande lorsque vous êtes freeze.");
        }
    }


}
