package fr.vexia.api.stats;

import fr.vexia.api.servers.GameType;

import java.util.HashMap;

public class Statistic {

    private final GameType gameType;
    private final HashMap<StatsType, Integer> stats;

    public Statistic(GameType gameType, HashMap<StatsType, Integer> stats) {
        this.gameType = gameType;
        this.stats = stats;
    }

    public GameType getGameType() {
        return gameType;
    }

    public HashMap<StatsType, Integer> getStats() {
        return stats;
    }

    public void add(StatsType statsType, Integer value) {
        if(!stats.containsKey(statsType)) {
            throw new IllegalArgumentException("Stats not contains "+statsType.name()+" !");
        }

        stats.put(statsType, stats.get(statsType) + value);
    }

    public void remove(StatsType statsType, Integer value) {
        if(!stats.containsKey(statsType)) {
            throw new IllegalArgumentException("Stats not contains "+statsType.name()+" !");
        }

        stats.put(statsType, stats.get(statsType) - value);
    }

    public void set(StatsType statsType, Integer value) {
        if(!stats.containsKey(statsType)) {
            throw new IllegalArgumentException("Stats not contains "+statsType.name()+" !");
        }

        stats.put(statsType, value);
    }

    public Integer get(StatsType statsType) {
        if(!stats.containsKey(statsType)) {
            throw new IllegalArgumentException("Stats not contains "+statsType.name()+" !");
        }

        return stats.get(statsType);
    }

}
