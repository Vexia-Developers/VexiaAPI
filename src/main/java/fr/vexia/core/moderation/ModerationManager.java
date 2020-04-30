package fr.vexia.core.moderation;

import fr.vexia.core.moderation.manager.VanishManager;

import java.util.UUID;

public class ModerationManager {

    private VanishManager vanishManager;

    public ModerationManager() {
        this.vanishManager = new VanishManager();
    }

    public boolean toggleVanish(UUID uuid) {
        return vanishManager.toggleVanish(uuid);
    }

    public boolean isVanish(UUID uuid) {
        return vanishManager.isVanish(uuid);
    }

}
