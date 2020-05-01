package fr.vexia.api.players.rank;

import net.md_5.bungee.api.ChatColor;

public enum Rank {

    JOUEUR( "Joueur", ChatColor.GRAY, 10),
    MINIVX( "Mini-VX", ChatColor.AQUA, 35),
    VX( "VX", ChatColor.YELLOW, 60),
    VX_PLUS( "VX+", ChatColor.GREEN, 75),
    PARTENAIRE("Partenaire", ChatColor.BLUE),
    AMI("Ami", ChatColor.WHITE),
    STAFF("Staff", ChatColor.DARK_GREEN),
    MODERATEUR("Modérateur", ChatColor.GOLD),
    ADMINISTRATEUR( "Admin", ChatColor.DARK_RED);

    private int id;
    private String name;
    private String prefix;
    private ChatColor color;
    private String tab;
    private int friendsMax;

    private Rank(String prefix, ChatColor color, int friendsMax) {
        this(prefix.toLowerCase().replace(". ", "-").toLowerCase(), prefix, color, friendsMax);
    }

    private Rank(String prefix, ChatColor color) {
        this(prefix.toLowerCase().replace(". ", "-").toLowerCase(), prefix, color, -1);
    }

    private Rank(String name, String prefix, ChatColor color, int friendsMax) {
        this.id = ordinal();
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.tab = String.format("%02d", ordinal());
        this.friendsMax = friendsMax;
    }

    public static Rank getRankById(int id) {
        for (Rank list : values()) {
            if (list.getId() == id) {
                return list;
            }
        }
        return null;
    }

    public static Rank getRankByName(String name) {
        for (Rank list : values()) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
    public String getColoredPrefix() {
        return color + "[" + prefix + "]";
    }

    public ChatColor getColor() {
        return color;
    }

    public String getTab() {
        return tab;
    }

    public int getFriendsMax() {
        return friendsMax;
    }
}
