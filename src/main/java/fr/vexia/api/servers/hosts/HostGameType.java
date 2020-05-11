package fr.vexia.api.servers.hosts;


public enum HostGameType {

    UHC("UHC Classique"),
    LGUHC("UHC Loup-Garous"),
    TaupeGunUHC("UHC TaupeGun");

    private final String name;

    HostGameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
