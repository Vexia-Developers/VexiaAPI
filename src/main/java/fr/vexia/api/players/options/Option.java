package fr.vexia.api.players.options;

import net.md_5.bungee.api.ChatColor;

public enum Option {

    SHOW_PLAYER(0, true, "Afficher les joueurs (Lobby)", "Cacher les joueurs sur le lobby"),
    SHOW_CHAT(0, true, "Afficher le chat (Lobby)", "Masquer le chat général des lobbys"),
    SHOW_EVENT(0, false, "Annonce de Host (Lobby)", "Afficher faire des annconce des serveurs host"),

    PRIVATE_MESSAGE(1, true, "Recevoir les messages privés", "Bloquer la réception de messages privés"),
    FRIEND_REQUEST(1, false, "Recevoir les demandes d'amis", "Bloquer la réception de demandes en ami"),
    PARTY_REQUEST(1, true, "Recevoir les demandes de party", "Bloquer la réception de demandes en party"),
    TEAM_REQUEST(1, true, "Recevoir les demandes de team", "Bloquer la réception de demandes de team"),

    SHOW_PARTICULE(2, false, "Afficher les particules", "Cacher les différentes particule", "➥  Pour réduire les lags"),
    FRIEND_NOTIFICATION(2, false, "Notifications co/deco des amis", "Notification de connection et de", "déconnection de mes amis"),
    PARTY_FOLLOW(2, false, "Suivre le capitaine de la party", "Suivre le capitaine de la party pour que quand", "il change de serveur je le suive");

    private int category;
    private boolean allowFriends;
    private String message;
    private String[] description;

    Option(int category, boolean allowFriends, String message, String... description) {
        this.category = category;
        this.allowFriends = allowFriends;
        this.message = message;
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public boolean isAllowFriends() {
        return allowFriends;
    }

    public String getMessage() {
        return message;
    }

    public String[] getDescription() {
        return description;
    }

    public static Option getOption(String message) {
        message = ChatColor.stripColor(message);
        for (Option value : values()) {
            if (value.getMessage().equalsIgnoreCase(message)) {
                return value;
            }
        }
        return null;
    }

    public enum OptionValue {
        ON("§aActivé"),
        OFF("§cDésactivé"),
        FRIENDS("§dAmis");

        private String formatName;

        OptionValue(String formatName) {
            this.formatName = formatName;
        }

        public String getFormatName() {
            return formatName;
        }
    }
}
