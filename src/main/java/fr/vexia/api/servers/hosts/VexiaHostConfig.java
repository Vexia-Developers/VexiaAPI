package fr.vexia.api.servers.hosts;

import java.util.UUID;

public class VexiaHostConfig {

    private int id;
    private UUID ownerUUID;
    private HostGameType type = HostGameType.UHC;

    private int maxPlayer = 20;
    private int teams = 8;
    private int borderSize = 1000;
    private int borderEndSize = 100;
    private float borderSpeed = 1F; // block / s
    private int borderReduce = 60; // in minutes
    private int timeBeforePVP = 10; // in minutes
    private boolean nether = true;

    private String serverName;

    public VexiaHostConfig(){}

    public VexiaHostConfig(int id, UUID ownerUUID, HostGameType type, String serverName,
                           int maxPlayer, int teams, int borderSize, int borderEndSize, float borderSpeed, int borderReduce, int timeBeforePVP, boolean nether) {
        this.id = id;
        this.ownerUUID = ownerUUID;
        this.type = type;
        this.serverName = serverName;
        this.maxPlayer = maxPlayer;
        this.teams = teams;
        this.borderSize = borderSize;
        this.borderEndSize = borderEndSize;
        this.borderSpeed = borderSpeed;
        this.borderReduce = borderReduce;
        this.timeBeforePVP = timeBeforePVP;
        this.nether = nether;
    }

    public int getId() {
        return id;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public HostGameType getType() {
        return type;
    }

    public void setType(HostGameType type) {
        this.type = type;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getTeams() {
        return teams;
    }

    public void setTeams(int teams) {
        this.teams = teams;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public float getBorderSpeed() {
        return borderSpeed;
    }

    public void setBorderEndSize(int borderEndSize) {
        this.borderEndSize = borderEndSize;
    }

    public int getBorderEndSize() {
        return borderEndSize;
    }

    public void setBorderSpeed(float borderSpeed) {
        this.borderSpeed = borderSpeed;
    }

    public int getBorderReduce() {
        return borderReduce;
    }

    public void setBorderReduce(int borderReduce) {
        this.borderReduce = borderReduce;
    }

    public int getTimeBeforePVP() {
        return timeBeforePVP;
    }

    public void setTimeBeforePVP(int timeBeforePVP) {
        this.timeBeforePVP = timeBeforePVP;
    }

    public boolean isNether() {
        return nether;
    }

    public void setNether(boolean nether) {
        this.nether = nether;
    }

    public String[] buildConfig(){
        return new String[]{
                "§eMode de jeu : §b" + getType().getName(),
                "§7- Joueurs max : §d" + getMaxPlayer(),
                "§7- Joueurs par équipe : §d" + getTeams(),
                "§7- Taille de la bordure : §d" + getBorderSize() + " blocks",
                "§7- Vitesse des bordures : §d" + getBorderSpeed() + " blocks/s",
                "§7- Réduction de la bordure à §d" + getBorderReduce() + " min",
                "§7 - Activation du PvP à §d" + getTimeBeforePVP() + " min"
        };
    }
}
