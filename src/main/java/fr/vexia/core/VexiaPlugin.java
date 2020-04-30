package fr.vexia.core;

import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.servers.VexiaServer;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VexiaPlugin extends JavaPlugin {

    private VexiaServer vexiaServer;

    @Override
    public void onEnable() {
        this.vexiaServer = this.onStart();
        ServerManager.save(vexiaServer);
    }

    @Override
    public void onDisable() {
        ServerManager.delete(vexiaServer);
        this.onStop();
    }

    public abstract VexiaServer onStart();

    public abstract void onStop();
}
