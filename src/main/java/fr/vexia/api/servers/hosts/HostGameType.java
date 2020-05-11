package fr.vexia.api.servers.hosts;


public enum HostGameType {

    UHC("UHC Classique", new String[]{}),
    LGUHC("UHC Loup-Garous", new String[]{}),
    TaupeGunUHC("UHC TaupeGun", new String[]{});

    private final String name;
    private final String[] description;

    HostGameType(String name, String[] description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }
}
