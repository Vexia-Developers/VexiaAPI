package fr.vexia.core.staff.vanish;

import fr.vexia.core.VexiaCore;
import fr.vexia.core.staff.StaffManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (StaffManager.get().isVanish(onlinePlayer)) {
                player.hidePlayer(VexiaCore.get(), onlinePlayer);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(StaffManager.get().isVanish(player)) {
            StaffManager.get().unVanish(player);
        }
    }

}
