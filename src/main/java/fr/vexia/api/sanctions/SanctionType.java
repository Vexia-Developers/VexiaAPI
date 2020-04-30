package fr.vexia.api.sanctions;

public enum SanctionType {

    BAN("Ban"),
    MUTE("Mute"),
    KICK("Kick");

    private final String name;

    SanctionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
