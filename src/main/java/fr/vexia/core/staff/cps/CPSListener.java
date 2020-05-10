package fr.vexia.core.staff.cps;

import fr.vexia.core.staff.StaffManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CPSListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        StaffManager.get().removeCPSPlayer(player);
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        CPSPlayer account = StaffManager.get().getCPSPlayer(player);

        Action action = event.getAction();
        if (event.getHand() != EquipmentSlot.OFF_HAND) {
            if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                account.addRightClick();
            } else if (action.equals(Action.LEFT_CLICK_AIR)) {
                account.addLeftClick();
            }
        }
    }
}
