package fr.vexia.core;

import fr.minuskube.inv.InventoryManager;
import fr.vexia.api.VexiaAPI;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.core.listeners.PlayerChatListener;
import fr.vexia.core.listeners.PlayerJoinListener;
import fr.vexia.core.manager.TeleportManager;
import fr.vexia.core.pubsub.TeleportPubSub;
import fr.vexia.core.staff.cps.CPSListener;
import fr.vexia.core.staff.cps.CPSTask;
import fr.vexia.core.staff.cps.VerifCommand;
import fr.vexia.core.staff.freeze.FreezeCommand;
import fr.vexia.core.staff.freeze.FreezeListener;
import fr.vexia.core.staff.mod.ModCommand;
import fr.vexia.core.staff.mod.ModListener;
import fr.vexia.core.staff.vanish.VanishCommand;
import fr.vexia.core.staff.vanish.VanishListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VexiaCore extends JavaPlugin {

    private VexiaAPI api;

    private static VexiaCore instance;
    private InventoryManager inventoryManager;

    public static VexiaCore get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        api = new VexiaAPI();
        api.init();

        TeleportManager teleportManager = new TeleportManager();

        this.inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(teleportManager), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);

        /*
        Moderation
         */
        pluginManager.registerEvents(new CPSListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new CPSTask(), 20L, 20L);
        new VerifCommand().register();

        pluginManager.registerEvents(new FreezeListener(), this);
        new FreezeCommand().register();

        pluginManager.registerEvents(new ModListener(), this);
        new ModCommand().register();

        pluginManager.registerEvents(new VanishListener(), this);
        new VanishCommand().register();


        PubSubAPI.psubscribe("teleport", new TeleportPubSub(teleportManager));
    }

    @Override
    public void onDisable() {
        api.stop();
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
