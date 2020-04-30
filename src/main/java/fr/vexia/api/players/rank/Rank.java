package fr.vexia.api.players.rank;

import net.md_5.bungee.api.ChatColor;

public enum Rank {

    ADMINISTRATEUR( "Admin", ChatColor.DARK_RED),
    MODERATEUR("Modérateur", ChatColor.GOLD),
    STAFF("Staff", ChatColor.DARK_GREEN),
    AMI("Ami", ChatColor.WHITE),
    PARTENAIRE("Partenaire", ChatColor.BLUE),
    VX_PLUS( "VX+", ChatColor.GREEN, 75),
    VX( "VX", ChatColor.YELLOW, 60),
    MINIVX( "Mini-VX", ChatColor.AQUA, 35),
    JOUEUR( "Joueur", ChatColor.GRAY, 10);

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
        this.id = ordinal() - values().length;
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
