package fr.vexia.api.stats;

public enum StatsType {

    WINS("Victoires"),
    LOOSES("Défaites"),
    KILLS("Kills"),
    DEATHS("Morts");

    private String name;

    StatsType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
