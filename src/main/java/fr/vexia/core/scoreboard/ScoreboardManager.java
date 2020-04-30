package fr.vexia.core.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Map<UUID, APersonalScoreboard> scoreboards;

    private int online = 0;

    public ScoreboardManager(Plugin plugin) {
        this.scoreboards = new HashMap<>();

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(plugin, () -> scheduler.runTaskAsynchronously(plugin, () -> {
            this.scoreboards.values().forEach(scoreboard -> scoreboard.update(online));
        }), 20, 20);

        /* plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            for (APersonalScoreboard scoreboard : this.scoreboards.values())
                new Thread(scoreboard::update).start();
        }, 20, 20); */
    }

    public void onDisable() {
        this.scoreboards.values().forEach(APersonalScoreboard::onLogout);
    }

    public void onLogin(APersonalScoreboard personalScoreboard) {
        if (this.scoreboards.containsKey(personalScoreboard.getPlayer().getUniqueId()))
            return;
        this.scoreboards.put(personalScoreboard.getPlayer().getUniqueId(), personalScoreboard);
    }

    public void onLogout(Player player) {
        if (this.scoreboards.containsKey(player.getUniqueId())) {
            this.scoreboards.get(player.getUniqueId()).onLogout();
            this.scoreboards.remove(player.getUniqueId());
        }
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
