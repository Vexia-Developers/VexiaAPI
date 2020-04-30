package fr.vexia.api.servers;

public enum GameState {

    WAITING(1, "STATE_WAITING"),
    GAME(2, "STATE_GAME"),
    FINISH(3, "STATE_FINISH");

    private int id;
    private String name;

    GameState(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
