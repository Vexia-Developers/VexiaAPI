package fr.vexia.proxy.utils;


import java.util.HashMap;

public enum TimeUnit {

    SECONDE("S", "sec", 1),
    MINUTE("M", "min", 60),
    HEURE("H", "h", 60 * 60),
    JOUR("J", "j", 60 * 60 * 24),
    MOIS("Mois", "m", 60 * 60 * 24 * 30);

    private String name;
    private String shortcut;
    private long toSecond;

    private static HashMap<String, TimeUnit> id_shortcuts = new HashMap<>();

    TimeUnit(String name, String shortcut, long toSecond) {
        this.name = name;
        this.shortcut = shortcut;
        this.toSecond = toSecond;
    }

    static {
        for(TimeUnit units : values()){
            id_shortcuts.put(units.shortcut, units);
        }
    }

    public static TimeUnit getFromShortcut(String shortcut){
        return id_shortcuts.get(shortcut);
    }

    public String getName(){
        return name;
    }

    public String getShortcut(){
        return shortcut;
    }

    public long getToSecond() {
        return toSecond;
    }

    public static boolean existFromShortcut(String shortcut){
        return id_shortcuts.containsKey(shortcut);
    }

}