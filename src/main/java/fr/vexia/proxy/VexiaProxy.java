package fr.vexia.proxy;

import fr.vexia.api.VexiaAPI;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.api.sanctions.SanctionType;
import fr.vexia.proxy.commands.admin.BroadcastCommand;
import fr.vexia.proxy.commands.admin.RestartCommand;
import fr.vexia.proxy.commands.admin.SetrankCommand;
import fr.vexia.proxy.commands.moderation.*;
import fr.vexia.proxy.commands.player.FriendsCommand;
import fr.vexia.proxy.listeners.*;
import fr.vexia.proxy.commands.player.HelpCommand;
import fr.vexia.proxy.commands.player.HubCommand;
import fr.vexia.proxy.commands.player.PingCommand;
import fr.vexia.proxy.manager.FriendsManager;
import fr.vexia.proxy.pubsub.ServerUpdater;
import fr.vexia.proxy.staff.StaffManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class VexiaProxy extends Plugin {

    private Configuration configuration;
    private StaffManager staffManager;
    private FriendsManager friendsManager;

    @Override
    public void onEnable() {
        VexiaAPI api = new VexiaAPI();
        api.init();

        this.staffManager = new StaffManager();
        this.friendsManager = new FriendsManager();

        initConfig();

        // Register Listeners
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new ProxyJoinListener(this));
        pluginManager.registerListener(this, new ProxyQuitListener(this));
        pluginManager.registerListener(this, new ProxyChatListener());
        pluginManager.registerListener(this, new ProxyPingListener(configuration));
        pluginManager.registerListener(this, new ProxyKickListener());

        new FriendsCommand(this).register();
        new HelpCommand(this).register();
        new HubCommand(this).register();
        new PingCommand(this).register();

        // Register Commands

        new TpCommand(this).register();
        new PlayerInfoCommand(this).register();
        new StaffCommand(this).register();
        new SanctionCommand(this, SanctionType.BAN).register();
        new SanctionCommand(this, SanctionType.KICK).register();
        new SanctionCommand(this, SanctionType.MUTE).register();
        new UnSanctionCommand(this, SanctionType.BAN).register();
        new UnSanctionCommand(this, SanctionType.MUTE).register();

        new BroadcastCommand(this).register();
        new RestartCommand(this).register();
        new SetrankCommand(this).register();
        //TODO: Moneyadd command
        
        PubSubAPI.psubscribe("serverupdater*", new ServerUpdater());
    }

    @Override
    public void onDisable() {

    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initConfig() {
        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();

            File file = new File(getDataFolder(), "config.yml");

            if (!file.exists()) {
                try (InputStream in = getResourceAsStream("configProxy.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    public FriendsManager getFriendsManager() {
        return friendsManager;
    }
}
