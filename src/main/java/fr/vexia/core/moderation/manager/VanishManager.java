package fr.vexia.core.moderation.manager;

import java.util.ArrayList;
import java.util.UUID;

public class VanishManager {

    private ArrayList<UUID> vanishes;
    public VanishManager() {
        this.vanishes = new ArrayList<>();
    }

    public boolean isVanish(UUID uuid) {
        return vanishes.contains(uuid);
    }

    public boolean toggleVanish(UUID uuid) {
        boolean isVanish = isVanish(uuid);

        if(isVanish) vanishes.remove(uuid);
        else vanishes.add(uuid);

        return !isVanish;
    }

}
