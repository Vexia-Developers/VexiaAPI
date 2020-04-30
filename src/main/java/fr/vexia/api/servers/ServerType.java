package fr.vexia.api.servers;

public enum ServerType {

    LOBBY(false),
    GAME(true),
    GAME_STATIC(false),
    HOST(true),
    OTHER(false);

    private boolean queue;

    ServerType(boolean queue) {
        this.queue = queue;
    }

    public boolean isQueue() {
        return queue;
    }

}
