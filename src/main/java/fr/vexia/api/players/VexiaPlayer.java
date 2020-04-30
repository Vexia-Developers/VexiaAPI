package fr.vexia.api.players;

import fr.vexia.api.players.options.Option;
import fr.vexia.api.players.rank.Rank;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class VexiaPlayer implements Cloneable {

    private UUID uuid;
    private String name;
    private String ip;
    private Rank rank;
    private int coins;
    private int credits;

    private Date firstJoin;
    private Date lastJoin;
    private Date rankExpires;

    private String server;
    private HashMap<Option, Option.OptionValue> options;

    public VexiaPlayer(UUID uuid, String name, String ip, Rank rank, int coins, int credits, Date firstJoin,
                       Date lastJoin, Date rankExpires, String server, HashMap<Option, Option.OptionValue> options) {
        this.uuid = uuid;
        this.name = name;
        this.ip = ip;
        this.rank = rank;
        this.coins = coins;
        this.credits = credits;
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.rankExpires = rankExpires;
        this.server = server;
        this.options = options;
    }

    public VexiaPlayer(UUID uuid, String name, String ip, String server) {
        this(uuid, name, ip, Rank.JOUEUR, 0, 0, new Date(), new Date(), null, server, new HashMap<>());
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Date getRankExpires() {
        return rankExpires;
    }

    public void setRankExpires(Date rankExpires) {
        this.rankExpires = rankExpires;
    }

    public Date getFirstJoin() {
        return firstJoin;
    }

    public Date getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(Date lastJoin) {
        this.lastJoin = lastJoin;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public HashMap<Option, Option.OptionValue> getOptions() {
        return options;
    }

    public Option.OptionValue getOption(Option option) {
        return options.getOrDefault(option, Option.OptionValue.ON);
    }

    @Override
    public String toString() {
        return "VexiaPlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", rank=" + rank +
                ", coins=" + coins +
                ", credits=" + credits +
                ", firstJoin=" + firstJoin +
                ", lastJoin=" + lastJoin +
                ", rankExpires=" + rankExpires +
                ", server='" + server + '\'' +
                '}';
    }
}