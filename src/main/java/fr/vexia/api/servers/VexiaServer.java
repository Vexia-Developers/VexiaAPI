package fr.vexia.api.servers;

public class VexiaServer {

    private String name;
    private ServerType serverType;
    private GameType gameType;
    private GameState state;
    private int online;
    private int max;
    private int port;

    public VexiaServer(String name, ServerType serverType, GameType gameType, GameState state, int online, int max,
                       int port) {
        this.name = name;
        this.serverType = serverType;
        this.gameType = gameType;
        this.state = state;
        this.online = online;
        this.max = max;
        this.port = port;
    }

    public VexiaServer(String name, ServerType serverType, GameType gameType, GameState state, int max, int port) {
        this(name, serverType,gameType, state,0,max, port);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getMax() {
        return max;
    }

    public int getPort() {
        return port;
    }
}