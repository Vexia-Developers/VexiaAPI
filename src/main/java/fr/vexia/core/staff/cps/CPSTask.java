package fr.vexia.core.staff.cps;

import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.core.staff.StaffManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CPSTask implements Runnable {

    private static final int MAX_CPS = 16;
    private StaffManager staffManager = StaffManager.get();

    @Override
    public void run() {
        staffManager.getCPSPlayers().forEach(this::checkCPS);
    }

    private void checkCPS(UUID uuid, CPSPlayer cpsPlayer) {
        if (cpsPlayer.getLeftClicks() >= MAX_CPS) {
            cpsPlayer.addAlertLeft();
            PubSubAPI.publish("cps-alert", String.format("%s:%s:%s:%s", "Gauche" , uuid, cpsPlayer.getLeftClicks(), cpsPlayer.getAlertLeft()));
        }

        if (cpsPlayer.getRightClicks() >= MAX_CPS) {
            cpsPlayer.addAlertRight();
            PubSubAPI.publish("cps-alert", String.format("%s:%s:%s:%s", "Droit" , uuid, cpsPlayer.getRightClicks(), cpsPlayer.getAlertRight()));
        }

        if(cpsPlayer.getMaxLeft() < cpsPlayer.getLeftClicks()) {
            cpsPlayer.setMaxLeft(cpsPlayer.getLeftClicks());
        }

        if(cpsPlayer.getMaxRight() < cpsPlayer.getRightClicks()) {
            cpsPlayer.setMaxRight(cpsPlayer.getRightClicks()<);
        }

        cpsPlayer.resetLeftClicks();
        cpsPlayer.resetRightClicks();

    }
}
