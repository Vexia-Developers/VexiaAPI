package fr.vexia.proxy.staff;

import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class StaffManager {

    private ArrayList<UUID> staff;

    public StaffManager() {
        this.staff = new ArrayList<>();
    }

    public ArrayList<UUID> getStaff() {
        return staff;
    }

    public void addStaff(UUID uuid) {
        staff.add(uuid);
    }

    public void removeStaff(UUID uuid) {
        staff.remove(uuid);
    }

    public boolean isStaff(UUID uuid) {
        return staff.contains(uuid);
    }

    public void broadcast(TextBuilder textBuilder) {
        for (UUID uuid : staff) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
            if(player == null) continue;

            player.sendMessage(textBuilder.build());
        }
    }

}
