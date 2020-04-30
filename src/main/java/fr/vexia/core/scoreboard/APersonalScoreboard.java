package fr.vexia.core.scoreboard;

import fr.vexia.api.data.manager.PlayerManager;
import org.bukkit.entity.Player;

import java.util.Random;

public abstract class APersonalScoreboard {

    protected final ObjectiveSign objectiveSign;
    protected final Player player;

    public APersonalScoreboard(Player player, PlayerManager playerManager, String name) {
        this.objectiveSign = new ObjectiveSign(String.valueOf(new Random().nextInt(999999999)), name);
        this.player = player;
        this.reloadData(playerManager);
        this.objectiveSign.addReceiver(player);
    }

    protected abstract void reloadData(PlayerManager playerManager);
    protected abstract void setLines(int online);

    void update(int online) {
        this.objectiveSign.clearScores();
        this.setLines(online);
        this.objectiveSign.updateLines();
    }

    void onLogout() {
        this.objectiveSign.removeReceiver(player);
    }

    public Player getPlayer() {
        return player;
    }
}
