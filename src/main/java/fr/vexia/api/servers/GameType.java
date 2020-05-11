package fr.vexia.api.servers;

import fr.vexia.api.stats.StatsType;

import static fr.vexia.api.stats.StatsType.*;

public enum GameType {

    UHC("Uhc", new StatsType[]{}),
    ISLANDFLAG("IslandFlag", new StatsType[]{WINS, LOOSES, KILLS, DEATHS}),
    RUSHBOX("RushBox", new StatsType[]{KILLS, DEATHS});

    private final String name;
    private final StatsType[] statsTypes;

    GameType(String name, StatsType[] statsTypes) {
        this.name = name;
        this.statsTypes = statsTypes;
    }

    public StatsType[] getStatsTypes() {
        return statsTypes;
    }

    public static GameType getGameType(String name) {
        for (GameType value : values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
