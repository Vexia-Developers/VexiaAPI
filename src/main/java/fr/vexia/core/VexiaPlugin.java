package fr.vexia.core;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.servers.VexiaServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VexiaPlugin extends JavaPlugin implements Listener {

    private VexiaServer vexiaServer;

    @Override
    public void onEnable() {
        this.vexiaServer = this.onStart();
        ServerManager.save(vexiaServer);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        ServerManager.delete(vexiaServer);
        this.onStop();
    }

    public abstract VexiaServer onStart();

    public abstract void onStop();

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        vexiaServer.setOnline(Bukkit.getOnlinePlayers().size());
        ServerManager.save(vexiaServer);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        vexiaServer.setOnline(Bukkit.getOnlinePlayers().size() - 1);
        ServerManager.save(vexiaServer);
    }
}
