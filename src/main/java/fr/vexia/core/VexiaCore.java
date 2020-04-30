package fr.vexia.core;

import fr.vexia.core.listeners.PlayerChatListener;
import fr.vexia.core.listeners.PlayerJoinListener;
import fr.vexia.core.manager.TeleportManager;
import fr.vexia.core.pubsub.TeleportPubSub;
import fr.vexia.api.VexiaAPI;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VexiaCore extends JavaPlugin {

    @Override
    public void onEnable() {
        VexiaAPI api = new VexiaAPI();
        api.init();

        TeleportManager teleportManager = new TeleportManager();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(teleportManager), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);

        PubSubAPI.psubscribe("teleport", new TeleportPubSub(teleportManager));
    }

}
